package com.aztrex.procgenjs.modules.serviceModules.workspace.service;

import com.aztrex.procgenjs.common.database.RoutingDataSource;
import com.aztrex.procgenjs.common.utility.classes.FileUtil;
import com.aztrex.procgenjs.common.utility.classes.JavaUtil;
import com.aztrex.procgenjs.common.utility.constant.CommonConstant;
import com.aztrex.procgenjs.common.utility.constant.PathConstant;
import com.aztrex.procgenjs.common.utility.constant.ProcgenjsConstant;
import com.aztrex.procgenjs.common.utility.database.DBUtil;
import com.aztrex.procgenjs.common.utility.exception.ResourceNotFoundException;
import com.aztrex.procgenjs.modules.serviceModules.workspace.database.model.WorkspaceRecord;
import com.aztrex.procgenjs.modules.serviceModules.workspace.database.repository.WorkspaceRepository;
import com.aztrex.procgenjs.modules.serviceModules.workspace.dto.config.WorkspaceConfig;
import com.aztrex.procgenjs.modules.serviceModules.workspace.dto.model.WorkspaceUIListItem;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

//https://dzone.com/articles/how-to-create-rest-api-with-spring-boot
//https://bezkoder.com/spring-boot-jpa-crud-rest-api/
//https://www.springboottutorial.com/spring-boot-crud-rest-service-with-jpa-hibernate

@Slf4j
@Data
@Service
public class WorkspaceService {
    public static final Set<String> IGNORE_PARAMETER_SET = new HashSet<String>(Set.of());

    // @Autowired
    // private WorkspaceDataSourceRegistry workspaceDataSourceRegistry;

    @Autowired
    @Lazy
    private WorkspaceRepository repository;

    @Autowired
    private RoutingDataSource routingDataSource;

    Pageable paging;
    Specification<WorkspaceRecord> specification;
    List<WorkspaceRecord> workspaceRecordList;
    Page<WorkspaceRecord> pagedResult;
    Map<String, Object> response;
    Object objectValue;

    private ArrayList<WorkspaceRecord> emptyRecord = new ArrayList<WorkspaceRecord>();

    @PostConstruct
    private void initializePostConstruct() {
        workspaceRecordList = repository.findAll();
        updateRoutingDataSource();
    }

    public WorkspaceRecord addEntry(WorkspaceRecord entry) {
        return repository.save(entry);
    }

    @Transactional
    public WorkspaceRecord updateEntry(Long id, WorkspaceRecord entry) {
        WorkspaceRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        record.setItemId(entry.getItemId());
        record.setTitle(entry.getTitle());
        record.setDetails(entry.getDetails());
        record.setPath(entry.getPath());
        record.setCategory(entry.getCategory());
        record.setGroup(entry.getGroup());
        record.setDateCreated(entry.getDateCreated());
        record.setDateUpdated(entry.getDateUpdated());
        record.setTags(entry.getTags());
        return repository.save(entry);
    }

    public WorkspaceRecord addOrUpdate(WorkspaceRecord entry) {
        Long id = entry.getId();
        if (Objects.nonNull(id) && id > 0) { // Update: Id is present and is more than 0.
            return updateEntry(entry.getId(), entry);
        } else { // Add: Id is null or 0.
            return addEntry(entry);
        }
    }

    public WorkspaceRecord getById(Long id) {
        WorkspaceRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        return record;
    }

    public List<WorkspaceRecord> getAll() {
        return repository.findAll();
    }

    public ResponseEntity<Map<String, Object>> search(Map<String, String> searchParams, Integer page, Integer size,
            String sort) {
        paging = PageRequest.of(page, size, Sort.by(sort));
        specification = (root, query, criteriaBuilder) -> {
            return JavaUtil.buildPredicateForSearchParamsByObject(searchParams, root, criteriaBuilder,
                    IGNORE_PARAMETER_SET);
        };

        pagedResult = repository.findAll(specification, paging);

        response = new HashMap<>();
        JavaUtil.populatePagedResultInResponse(response, pagedResult);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteEntry(WorkspaceRecord entry) {

        if (checkEntryAndIdExists(entry)) {
            repository.delete(entry);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteById(Long id) {
        if (checkExistsById(id)) {
            repository.deleteById(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean checkExistsById(Long id) {
        return repository.existsById(id);
    }

    private boolean checkEntryAndIdExists(WorkspaceRecord entry) {
        return Objects.nonNull(entry) && Objects.nonNull(entry.getId()) && repository.existsById(entry.getId());
    }

    public void createWorkspace(String targetDir, String folderName) throws IOException {
        Path destinationPath = Paths.get(targetDir, folderName);

        if (Files.exists(destinationPath)) {
            throw new IOException("Workspace already exists.");
        }

        copyDirectory(Paths.get(PathConstant.WORKSPACE_TEMPLATE_PATH_TO_COPY), destinationPath);
        updateWorkspaceJson(destinationPath, folderName);
        // WorkspaceRecord newWorkspaceRecord = updateWorkspaceJson(destinationPath,
        // folderName); // Modified to return WorkspaceRecord
        //
        // // Add the new workspace record to the list and update the RoutingDataSource
        // workspaceRecordList.add(newWorkspaceRecord);
        // updateRoutingDataSource(); // Update RoutingDataSource after adding the new
        // workspace

    }

    public void copyDirectory(Path source, Path destination) throws IOException {
        Files.walk(source).forEach(sourcePath -> {
            try {
                Path targetPath = destination.resolve(source.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(targetPath);
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error copying workspace", e);
            }
        });
    }

    private void updateWorkspaceJson(Path destinationPath, String workspaceName) throws IOException {
        updateWorkspaceJsonPaths(destinationPath, workspaceName);
    }

    // private WorkspaceRecord updateWorkspaceJson(Path destinationPath, String
    // workspaceName) throws IOException {
    // Path workspaceJsonPath =
    // destinationPath.resolve(PathConstant.WORKSPACE_JSON);
    //
    // if (!Files.exists(workspaceJsonPath)) {
    // throw new IOException("workspace.json not found in the copied directory: " +
    // workspaceJsonPath.toAbsolutePath());
    // }
    //
    // WorkspaceConfig workspaceConfig =
    // FileUtil.loadFromFile(workspaceJsonPath.toFile(), WorkspaceConfig.class);
    //
    // String newWorkspaceFolderPath = destinationPath.toAbsolutePath().toString();
    // workspaceConfig.setWorkspaceFolderPath(newWorkspaceFolderPath);
    // workspaceConfig.setTitle(workspaceName);
    // // ... (Update other paths in workspaceConfig) ...
    // workspaceConfig.setDatabasePath(String.format("%s/%s.db",
    // workspaceConfig.getDatabaseFolderPath(), workspaceConfig.getDbname()));
    //
    // FileUtil.saveToFile(workspaceConfig, workspaceJsonPath.toFile());
    //
    // // Create the WorkspaceRecord and set the WorkspaceConfig
    // WorkspaceRecord newWorkspaceRecord = workspaceUIListItem.toWorkspaceRecord();
    // newWorkspaceRecord.setWorkspaceConfig(workspaceConfig);
    // repository.save(newWorkspaceRecord); // Save to database
    //
    // return newWorkspaceRecord;
    // }

    private void updateWorkspaceJsonPaths(Path destinationPath, String workspaceName) throws IOException {
        // Construct the path to the workspace.json file
        Path workspaceJsonPath = destinationPath.resolve(PathConstant.WORKSPACE_JSON);

        // Check if the workspace.json file exists
        if (!Files.exists(workspaceJsonPath)) {
            throw new IOException(
                    "workspace.json not found in the copied directory: " + workspaceJsonPath.toAbsolutePath());
        }

        // Load the WorkspaceConfig from the JSON file
        WorkspaceConfig workspaceConfig = FileUtil.loadFromFile(workspaceJsonPath.toFile(), WorkspaceConfig.class);

        // Update the paths
        String newWorkspaceFolderPath = destinationPath.toAbsolutePath().toString();
        // workspaceConfig.setId(workspaceUIListItem.getWsid());
        workspaceConfig.setWorkspaceFolderPath(newWorkspaceFolderPath);
        workspaceConfig.setTitle(workspaceName);
        workspaceConfig.setLibraryFolderPath(String.format("%s/library", workspaceConfig.getWorkspaceFolderPath()));
        workspaceConfig.setConfigurationFolderPath(
                String.format("%s/configuration", workspaceConfig.getWorkspaceFolderPath()));
        workspaceConfig.setDatabaseFolderPath(String.format("%s/database", workspaceConfig.getWorkspaceFolderPath()));
        workspaceConfig.setFilesPath(String.format("%s/files", workspaceConfig.getWorkspaceFolderPath()));
        workspaceConfig.setImageFolderPath(String.format("%s/image", workspaceConfig.getWorkspaceFolderPath()));
        workspaceConfig.setThemeFolderPath(String.format("%s/theme", workspaceConfig.getWorkspaceFolderPath()));
        workspaceConfig
                .setScriptFolderPath(String.format("%s/script/general", workspaceConfig.getWorkspaceFolderPath()));
        workspaceConfig.setWorkspaceJsonPath(
                String.format("%s/%s", workspaceConfig.getWorkspaceFolderPath(), PathConstant.WORKSPACE_JSON));
        workspaceConfig.setDatabasePath(
                String.format("%s/%s.db", workspaceConfig.getDatabaseFolderPath(), workspaceConfig.getDbname()));

        // Save the updated WorkspaceConfig back to the JSON file
        FileUtil.saveToFile(workspaceConfig, workspaceJsonPath.toFile());
    }

    public void removeWorkspace(Long id) {
        WorkspaceRecord workspaceRecord = repository.findById(id).orElse(null);
        if (Objects.nonNull(workspaceRecord)) {
            repository.deleteById(id);
        }
    }

    public String chooseTargetDirectory(String targetDirectory) {
        File selectedFolder = new File(targetDirectory);
        if (Objects.nonNull(selectedFolder)) {
            return selectedFolder.getAbsolutePath();
        }
        return null;
    }

    public List<WorkspaceUIListItem> getWorkspaceUIList() {
        return repository.findAll().stream()
                .map(WorkspaceUIListItem::new)
                .collect(Collectors.toList());
    }

    public ResponseEntity<Map<String, Object>> getAll(Integer page, Integer size, String sort) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sort));
        Page<WorkspaceRecord> pagedResult = repository.findAll(paging);

        Map<String, Object> response = new HashMap<>();
        response.put(CommonConstant.CONTENT, pagedResult.getContent());
        response.put(CommonConstant.CURRENT_PAGE, pagedResult.getNumber());
        response.put(CommonConstant.TOTAL_ITEMS, pagedResult.getTotalElements());
        response.put(CommonConstant.TOTAL_PAGES, pagedResult.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public WorkspaceRecord updateWorkspace(WorkspaceUIListItem workspaceUIListItem) {
        WorkspaceRecord record = new WorkspaceRecord();
        record.setId(workspaceUIListItem.getId());
        record.setTitle(workspaceUIListItem.getTitle());
        record.setCategory(workspaceUIListItem.getCategory());
        record.setGroup(workspaceUIListItem.getGroup());
        record.setDetails(workspaceUIListItem.getDetails());
        record.setTags(workspaceUIListItem.getTags());
        record.setPath(workspaceUIListItem.getPath());
        return repository.save(record);
    }

//    public WorkspaceContent getWorkspaceContent(Long id) {
//        WorkspaceRecord record = getById(id);
//        try {
//            WorkspaceConfig config = loadWorkspaceConfig(record);
//            if (config == null) {
//                throw new ResourceNotFoundException("Workspace config not found");
//            }
//
//            WorkspaceContent content = new WorkspaceContent();
//            content.setAssets(listFiles(config.getFilesPath()));
//            content.setDatabases(listFiles(config.getDatabaseFolderPath()));
//            content.setScripts(listFiles(config.getScriptFolderPath()));
//            content.setLibraries(listFiles(config.getLibraryFolderPath()));
//            content.setJavaFiles(listJavaFiles(config.getScriptFolderPath()));
//
//            return content;
//        } catch (IOException e) {
//            throw new RuntimeException("Error loading workspace content", e);
//        }
//    }

    private List<String> listFiles(String pathStr) {
        if (pathStr == null) return Collections.emptyList();
        File folder = new File(pathStr);
        if (!folder.exists() || !folder.isDirectory()) return Collections.emptyList();

        File[] files = folder.listFiles();
        if (files == null) return Collections.emptyList();

        return Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    private List<String> listJavaFiles(String pathStr) {
        if (pathStr == null) return Collections.emptyList();
        File folder = new File(pathStr);
        if (!folder.exists() || !folder.isDirectory()) return Collections.emptyList();

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".java"));
        if (files == null) return Collections.emptyList();

        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    private void updateRoutingDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        // Add tracker datasource
        targetDataSources.put(ProcgenjsConstant.PROCGENJS,
                DBUtil.createSqliteDataSource(PathConstant.PROCGENJS_DATABASE_PATH));

        for (WorkspaceRecord record : workspaceRecordList) {
            try {
                // Load WorkspaceConfig from JSON
                WorkspaceConfig config = loadWorkspaceConfig(record);
                if (config != null) {
                    String dbPath = config.getDatabasePath();
                    // Error Handling: Check if the database file exists
                    File dbFile = new File(dbPath);
                    if (!dbFile.exists()) {
                        log.error("Database file not found: {}", dbPath); // todo - add exception if file not found
                        continue; // Skip this workspace if the DB file is missing
                    }
                    targetDataSources.put(record.getItemId(), DBUtil.createSqliteDataSource(dbPath));
                }
            } catch (IOException e) {
                log.error("Error loading WorkspaceConfig for workspace {}: {}", record.getId(), e.getMessage());
            }
        }
        routingDataSource.setTargetDataSources(targetDataSources); // could be removed
        routingDataSource.afterPropertiesSet();
    }

    private WorkspaceConfig loadWorkspaceConfig(WorkspaceRecord record) throws IOException {
        Path jsonPath = Paths.get(record.getPath(), record.getTitle(), PathConstant.WORKSPACE_JSON_SUB_PATH);
        File jsonFile = jsonPath.toFile();
        if (jsonFile.exists()) {
            return FileUtil.loadFromFile(jsonFile, WorkspaceConfig.class);
        } else {
            log.warn("Workspace config JSON not found: {}", jsonPath);
            return null;
        }
    }
}
