package com.aztrex.procgenjs.modules.serviceModules.controller;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Service
public class DynamicPluginLoader {

    private final ConfigurableApplicationContext context;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    // Keep track of loaders to prevent GC issues or to allow unloading (optional)
    private final Map<String, URLClassLoader> workspaceLoaders = new HashMap<>();

    public DynamicPluginLoader(ApplicationContext context, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.context = (ConfigurableApplicationContext) context;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    /**
     * Main entry point: Loads everything from a specific folder
     */
    public void loadPluginFolder(String workspaceId, String folderPath) throws Exception {
        File root = new File(folderPath);
        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder: " + folderPath);
        }

        List<URL> urls = new ArrayList<>();
        List<String> binaryClassNames = new ArrayList<>();
        List<File> sourceFiles = new ArrayList<>();

        // 1. SCAN FOLDER CONTENTS
        Files.walkFileTree(root.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileName = file.getFileName().toString();
                
                if (fileName.endsWith(".jar")) {
                    // JAR FILE
                    urls.add(file.toUri().toURL());
                    binaryClassNames.addAll(scanJarForClasses(file.toFile()));
                } else if (fileName.endsWith(".class")) {
                    // UNPACKED CLASS FILE
                    // We don't add the file URL, we add the ROOT folder URL later.
                    // We just need to calculate the class name (com.foo.Bar) from the path.
                    String relPath = root.toPath().relativize(file).toString();
                    binaryClassNames.add(convertPathToClassName(relPath));
                } else if (fileName.endsWith(".java")) {
                    // SOURCE FILE
                    sourceFiles.add(file.toFile());
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // Add the root folder itself to classpath for unpacked classes
        urls.add(root.toURI().toURL());

        // 2. COMPILE JAVA FILES (If any)
        if (!sourceFiles.isEmpty()) {
            File outputDir = new File(root, "bin_gen");
            outputDir.mkdirs();
            urls.add(outputDir.toURI().toURL()); // Add compile output to classpath
            
            compileJavaFiles(sourceFiles, outputDir);
            
            // Add compiled classes to list
            for (File src : sourceFiles) {
                // Assumption: file structure matches package, or simple flat structure for demo
                // A robust compiler util would parse 'package' decl from file. 
                // For now, we rely on the compiler outputting them effectively.
                // We scan the outputDir to find what was generated.
                binaryClassNames.addAll(scanDirForClasses(outputDir, outputDir));
            }
        }

        // 3. CREATE CLASSLOADER
        // Parent is the current thread's loader (Spring Boot's loader) so we can see Host classes
        URLClassLoader classLoader = URLClassLoader.newInstance(
                urls.toArray(new URL[0]), 
                this.getClass().getClassLoader()
        );
        workspaceLoaders.put(workspaceId, classLoader);

        // 4. LOAD AND REGISTER BEANS
        for (String className : binaryClassNames) {
            try {
                Class<?> loadedClass = classLoader.loadClass(className);
                registerAsSpringBean(loadedClass);
            } catch (ClassNotFoundException e) {
                System.err.println("Could not load class: " + className);
            } catch (NoClassDefFoundError e) {
                System.err.println("Skipping " + className + " due to missing dependency: " + e.getMessage());
            }
        }
    }

    // --- HELPER: Register Bean & Controller ---
    private void registerAsSpringBean(Class<?> clazz) {
        // Only register if it has Spring annotations
        boolean isComponent = clazz.isAnnotationPresent(org.springframework.stereotype.Component.class) ||
                              clazz.isAnnotationPresent(org.springframework.stereotype.Service.class) ||
                              clazz.isAnnotationPresent(org.springframework.stereotype.Repository.class) ||
                              clazz.isAnnotationPresent(RestController.class);

        if (!isComponent) return;

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        String beanName = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);

        if (beanFactory.containsBeanDefinition(beanName)) {
            System.out.println("Reloading existing bean: " + beanName);
            beanFactory.removeBeanDefinition(beanName);
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        BeanDefinition beanDefinition = builder.getBeanDefinition();
        
        // This line ensures @Autowired works inside the dynamic bean
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        
        // Force initialization now to trigger @PostConstruct and Autowiring
        Object beanInstance = context.getBean(beanName);

        // If it's a Controller, register endpoints manually
        if (clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(org.springframework.stereotype.Controller.class)) {
            registerControllerEndpoints(beanInstance);
        }

        System.out.println("Successfully registered dynamic bean: " + beanName);
    }

    private void registerControllerEndpoints(Object bean) {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getMethods();

        try {
            // REFLECTION HACK: Access the protected method 'getMappingForMethod'
            // This method parses @GetMapping, @PostMapping etc. into a RequestMappingInfo
            Method getMappingForMethod = RequestMappingHandlerMapping.class
                    .getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
            getMappingForMethod.setAccessible(true);

            for (Method method : methods) {
                // Invoke the protected method manually
                RequestMappingInfo mappingInfo = (RequestMappingInfo) getMappingForMethod.invoke(requestMappingHandlerMapping, method, clazz);

                if (mappingInfo != null) {
                    // Register the mapping (this method is Public, so no reflection needed here)
                    // Note: unregisterMapping might be needed if reloading to prevent duplicates
                    try {
                        requestMappingHandlerMapping.unregisterMapping(mappingInfo);
                    } catch (Exception e) {
                        // Ignore if not exists
                    }

                    requestMappingHandlerMapping.registerMapping(mappingInfo, bean, method);
                    System.out.println("Registered Endpoint: " + mappingInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to register controller endpoints: " + e.getMessage());
        }
    }

    // --- HELPER: Compiler ---
    private void compileJavaFiles(List<File> sourceFiles, File outputDir) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        
        // Build Classpath: Current System Classpath + The folder's jars
        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(outputDir.getAbsolutePath());
        
        // NOTE: In a real Spring Boot JAR, calculating classpath for javac is tricky.
        // This simple setup assumes running in IDE or unpacked. 
        // For Fat JARs, you need to extract BOOT-INF/lib or build a string from current ClassLoader URLs.

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
        fileManager.close();
    }

    // --- HELPER: Utilities ---
    private List<String> scanJarForClasses(File jarFile) throws IOException {
        List<String> classes = new ArrayList<>();
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
                    classes.add(convertPathToClassName(entry.getName()));
                }
            }
        }
        return classes;
    }

    private List<String> scanDirForClasses(File root, File current) {
        List<String> classes = new ArrayList<>();
        File[] files = current.listFiles();
        if(files == null) return classes;

        for (File f : files) {
            if (f.isDirectory()) {
                classes.addAll(scanDirForClasses(root, f));
            } else if (f.getName().endsWith(".class")) {
                String rel = root.toPath().relativize(f.toPath()).toString();
                classes.add(convertPathToClassName(rel));
            }
        }
        return classes;
    }

    private String convertPathToClassName(String path) {
        return path.replace(".class", "")
                   .replace("/", ".")
                   .replace("\\", ".");
    }
}