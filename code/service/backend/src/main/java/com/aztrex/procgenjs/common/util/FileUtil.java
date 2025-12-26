package com.aztrex.procgenjs.common.util;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class FileUtil {

    /**
     * Filters the list of files to include only those with a .java extension.
     *
     * @param files the list of files to filter.
     * @return a list of files with the .java extension.
     */
    public static List<File> getFilesByExtension(List<File> files, String extension) {
        return files.stream()
                .filter(file -> file.isFile() && file.getName().endsWith(extension))
                .collect(Collectors.toList());
    }

    public static List<String> getFilePaths(List<File> files) {
        return files.stream()
                .map(File::getAbsolutePath) // or .map(File::getPath) if you want the relative path
                .collect(Collectors.toList());
    }

    public static List<Path> getFilePathsByExtension(List<File> files, String extension) {
        return files.stream()
                .filter(file -> file.isFile() && file.getName().endsWith(extension))
                .map(File::getAbsolutePath) // or .map(File::getPath) if you want the relative path
                .map(Paths::get)
                .collect(Collectors.toList());
    }

    public static String getFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        return getFileNameWithoutExtension(fileName);
    }

    public static @NotNull String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) { // Check if '.' is found and not the first/last character
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }
//
//    public static void addCheckBoxListenerForTreeItem(CheckBox checkBox, TreeItem<File> treeItem) {
//        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//            if (treeItem != null) {
//                if (treeItem.getGraphic() == null) {
//                    // Create a placeholder graphic if none exists
//                    treeItem.setGraphic(new CheckBox());  // Using CheckBox as a placeholder
//                }
//
//                BooleanProperty selectedProperty = (BooleanProperty) treeItem.getGraphic().getUserData();
//                if (selectedProperty != null) {
//                    selectedProperty.set(newValue);
//                } else {
//                    BooleanProperty newSelectedProperty = new SimpleBooleanProperty(newValue);
//                    treeItem.getGraphic().setUserData(newSelectedProperty);
//                }
//            }
//        });
//    }
//
//    public static void updateCheckBoxTreeItem(TreeItem<File> treeItem, CheckBox selectCheckBox, File item, boolean empty) {
//        if (empty || item == null) {
//            selectCheckBox.setGraphic(null);
//        } else {
//            if (treeItem != null) {
//                BooleanProperty selectedProperty = treeItem.getGraphic() == null ? null : (BooleanProperty) treeItem.getGraphic().getUserData();
//                if (selectedProperty != null) {
//                    selectCheckBox.setSelected(selectedProperty.get());
//                } else {
//                    selectCheckBox.setSelected(false);
//                }
//            }
//        }
//    }
//
//    public static String getWindowsPath(String filePath) {
//        if (filePath.startsWith(ForwardSlash) && System.getProperty(OsName).toLowerCase().contains(Win)) {
//            return filePath.substring(1);  // Remove leading slash for Windows
//        }
//        return filePath;
//    }
//
//    public static String getFileExtension(String fileName) {
//        int dotIndex = fileName.lastIndexOf(Dot);
//        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
//            return fileName.substring(dotIndex + 1).toLowerCase();
//        }
//        return Empty;
//    }
//
//    public static String getFolderNameFromString(String folderPath) {
//        return folderPath.substring(
//                Math.max(folderPath.lastIndexOf(ForwardSlash),
//                        folderPath.lastIndexOf(DoubleBackwardSlash)) + 1);
//    }
//
    // Static method to save any object to a JSON file
    public static void saveToFile(Object object, File file) throws IOException {
        JsonUtil.objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
    }
//
    // Static method to load an object from a JSON file
    public static <T> T loadFromFile(File file, Class<T> clazz) throws IOException {
        return JsonUtil.objectMapper.readValue(file, clazz);
    }
//
//    public static void createDirectoryIfNotExists(String path) {
//        File directory = new File(path);
//        if (!directory.exists()) {
//            if (directory.mkdirs()) {
//
//            } else {
//
//            }
//        } else {
//
//        }
//    }
//
//    public List<File> getSelectedFiles(TreeItem<File> rootItem) {
//        List<File> selectedFiles = new ArrayList<>();
//        if (rootItem != null) {
//            BooleanProperty selectedProperty = rootItem.getGraphic() == null ? null : (BooleanProperty) rootItem.getGraphic().getUserData();
//            if (selectedProperty != null && selectedProperty.get()) {
//                selectedFiles.add(rootItem.getValue());
//            }
//
//            for (TreeItem<File> child : rootItem.getChildren()) {
//                selectedFiles.addAll(getSelectedFiles(child));
//            }
//        }
//        return selectedFiles;
//    }
//
//    /**
//     * Retrieves a list of selected files from the given root item.
//     * If a directory is selected, all files within the directory are included unless specific files are selected.
//     *
//     * @param rootItem the root TreeItem representing a folder or file.
//     * @return a list of selected files.
//     */
//    public static List<File> getSelectedFilesFromRootItem(TreeItem<File> rootItem) {
//        List<File> selectedFiles = new ArrayList<>();
//        if (rootItem != null) {
//            BooleanProperty selectedProperty = rootItem.getGraphic() == null ? null : (BooleanProperty) rootItem.getGraphic().getUserData();
//
//            if (selectedProperty != null && selectedProperty.get()) {
//                File rootFile = rootItem.getValue();
//
//                // If the root item is a directory
//                if (rootFile != null && rootFile.isDirectory()) {
//                    // Check if any children are selected
//                    boolean hasSelectedChildren = rootItem.getChildren().stream()
//                            .anyMatch(child -> {
//                                BooleanProperty childSelectedProperty = child.getGraphic() == null ? null : (BooleanProperty) child.getGraphic().getUserData();
//                                return childSelectedProperty != null && childSelectedProperty.get();
//                            });
//
//                    // If no children are selected, add all files from the directory
//                    if (!hasSelectedChildren) {
//                        addAllFilesInDirectory(rootFile, selectedFiles);
//                    } else {
//                        selectedFiles.add(rootFile);
//                    }
//                } else {
//                    // If it's a file, add it to the list
//                    selectedFiles.add(rootFile);
//                }
//            }
//
//            // Recursively check children
//            for (TreeItem<File> child : rootItem.getChildren()) {
//                selectedFiles.addAll(getSelectedFilesFromRootItem(child));
//            }
//        }
//        return selectedFiles;
//    }
//
//    /**
//     * Adds all files from the specified directory to the list.
//     *
//     * @param directory the directory from which to retrieve files.
//     * @param fileList  the list to add files to.
//     */
//    private static void addAllFilesInDirectory(File directory, List<File> fileList) {
//        File[] files = directory.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile()) {
//                    fileList.add(file);
//                } else if (file.isDirectory()) {
//                    addAllFilesInDirectory(file, fileList); // Recursively add files from subdirectories
//                }
//            }
//        }
//    }
//
//    /**
//     * Loads a classpath from a file if it exists, otherwise generates a new classpath
//     * from the JARs in the target directory and saves it to the file.
//     *
//     * @param classpathDirectory The directory where classpathFileName is located.
//     * @param classpathFileName  The name of the classpath file.
//     * @return The classpath string.
//     */
//    public static String getClasspathFileByLoadOrCreate(String classpathDirectory, String subFolderClasspath, String classpathFileName) {
//        String classpath = "";
//        String classpathFilePath = classpathDirectory + subFolderClasspath + classpathFileName;
//        Path classpathFile = Paths.get(classpathFilePath);
//
//        if (Files.exists(classpathFile)) {
//            try {
//                List<String> lines = Files.readAllLines(classpathFile);
//                if (!lines.isEmpty()) { // Check if the list is empty
//                    classpath = lines.get(0);
//                }
//                log.info("Loaded classpath from: {}", classpathFile);
//            } catch (IOException e) {
//                log.error("Error loading the classpath file: {}", classpathFile, e);
//            }
//        }
//
//        // If the classpath was not loaded from a file, generate it
//        if (classpath.isEmpty()) {
//            try {
//                classpath = Files.walk(Paths.get(classpathDirectory))
//                        .filter(Files::isRegularFile)
//                        .filter(path -> path.toString().endsWith(".jar"))
//                        .map(Path::toAbsolutePath)
//                        .map(Path::toString)
//                        .collect(Collectors.joining(PathSeparator));
//            } catch (IOException e) {
//                log.error("Error walking through the directory: {}", classpathDirectory, e);
//                throw new RuntimeException(e);
//            }
//
////            classpath = classpath + PathSeparator + targetDirectory + "/classes";
//            try {
//                Files.write(classpathFile, List.of(classpath));
//                log.info("Created new classpath file: {}", classpathFile);
//            } catch (IOException e) {
//                log.error("Error saving the classpath file: {}", classpathFile, e);
//            }
//        }
//
//        return classpath;
//    }

}
