package com.aztrex.procgenjs.modules.serviceModules.workspace.service;// package com.aztrex.common.modules.workspace.service;
//
// import com.procgenfx.database.data.workspace.WorkspaceDao;
// import com.procgenfx.dto.data.workspace.WorkspaceConfig;
// import com.procgenfx.dto.data.workspace.item.WorkspaceUIListItem;
// import com.procgenfx.dto.state.AppState;
// import com.procgenfx.service.data.DataService;
// import com.procgenfx.util.constants.CommonConstant;
// import com.procgenfx.util.constants.PathConstant;
// import com.procgenfx.util.service.FileUtil;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import lombok.Getter;
// import lombok.Setter;
// import lombok.extern.slf4j.Slf4j;
//
// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.stream.Collectors;
//
// @Slf4j
// @Setter
// @Getter
// public class WorkspaceService1 {
// public static WorkspaceService1 instance;
//
// private AppState appState;
// private WorkspaceConfig dataConfig = new WorkspaceConfig();
// private WorkspaceConfig workspaceConfig = new WorkspaceConfig();
// private WorkspaceDao workspaceDao = new WorkspaceDao();
//
// private DataService dataService;
//
// private ObservableList<WorkspaceUIListItem> workspaceList =
// FXCollections.observableArrayList();
//
// private Map<String, WorkspaceConfig> workspaceConfigMap = new HashMap<>();
//
// @Getter
// @Setter
// private Map<String, Object> dataMap;
//
// public WorkspaceService1() {
// instance = this;
// }
//
// //@PostConstruct
// private void initializePostConstruct() {
// dataConfig = new WorkspaceConfig();
// workspaceConfig = new WorkspaceConfig();
//
// dataConfig.setTitlePath(CommonConstant.Data, PathConstant.DATA_PATH);
//
//// initializeAndLoadConfig(dataConfig);
// initializeAndLoadConfig(workspaceConfig);
//
//// workspaceConfigMap.put(workspaceConfig.);
// workspaceConfigMap.put("1", dataConfig);
// workspaceConfigMap.put("2", workspaceConfig);
// }
//
// public void initialize() {
// initializeService();
//
// }
//
// private void initializeService() {
//// workspaceDao = WorkspaceDao.get();
//// dataConfig = new WorkspaceConfig();
//// workspaceConfig = new WorkspaceConfig();
// workspaceDao.initialize();
//
// dataConfig.setTitlePath(CommonConstant.Data, PathConstant.DATA_PATH);
//
//// initializeAndLoadConfig(dataConfig);
// initializeAndLoadConfig(workspaceConfig);
//
// workspaceConfigMap.put("1", dataConfig);
// workspaceConfigMap.put("2", workspaceConfig);
// // workspaceConfigMap.put("3", workspaceConfig);
//// workspaceConfigMap.put("4", workspaceConfig);
//// workspaceConfigMap.put("5", workspaceConfig);
//// workspaceConfigMap.put("6", workspaceConfig);
//// workspaceConfigMap.put("7", workspaceConfig);
//// workspaceConfigMap.put("8", workspaceConfig);
//// workspaceConfigMap.put("9", workspaceConfig);
//// workspaceConfigMap.put("10", workspaceConfig);
//// workspaceConfigMap.put("11", workspaceConfig);
//// workspaceConfigMap.put("12", workspaceConfig);
//// workspaceConfigMap.put("13", workspaceConfig);
//// workspaceConfigMap.put("14", workspaceConfig);
//
//
// }
//
// public void setUpPostConstruct() {
// workspaceDao.setUpPostConstruct();
//
// populateWorkspaceList();
// }
//
// private void initializeAndLoadConfig(WorkspaceConfig workspaceConfig) {
// workspaceConfig.initialize();
// loadOrCreateConfig(workspaceConfig);
//// createDataBase(workspaceConfig);
//
// }
//
// private WorkspaceConfig loadOrCreateConfig(WorkspaceConfig config) {
//
// File configFile = new File(config.getWorkspaceJsonPath());
//
// if (!configFile.exists()) {
// createFolders(config);
//// createDataBase(config);
// return saveDefaultConfig(config, configFile);
// } else {
// return loadExistingConfig(configFile);
// }
// }
//
// private WorkspaceConfig saveDefaultConfig(WorkspaceConfig workspaceConfig,
// File configFile) {
// try {
// FileUtil.saveToFile(workspaceConfig, configFile);
// log.info("Created default config at: " + configFile.getAbsolutePath());
// return workspaceConfig;
// } catch (IOException e) {
// e.printStackTrace();
// return null;
// }
// }
//
// private WorkspaceConfig loadExistingConfig(File configFile) {
// try {
// return FileUtil.loadFromFile(configFile, WorkspaceConfig.class);
// } catch (IOException e) {
// e.printStackTrace();
// return null;
// }
// }
//
// private void createFolders(WorkspaceConfig workspaceConfig) {
// FileUtil.createDirectoryIfNotExists(workspaceConfig.getWorkspaceFolderPath());
// FileUtil.createDirectoryIfNotExists(workspaceConfig.getLibraryFolderPath());
// FileUtil.createDirectoryIfNotExists(workspaceConfig.getDatabaseFolderPath());
// FileUtil.createDirectoryIfNotExists(workspaceConfig.getFilesPath());
// FileUtil.createDirectoryIfNotExists(workspaceConfig.getImageFolderPath());
// FileUtil.createDirectoryIfNotExists(workspaceConfig.getThemeFolderPath());
// FileUtil.createDirectoryIfNotExists(workspaceConfig.getScriptFolderPath());
// }
//
// public ObservableList<WorkspaceUIListItem> getWorkspaceListFX() {
// return workspaceList;
// }
//
// public List<WorkspaceUIListItem> getWorkspaceUIList() {
// return workspaceDao.getWorkspaceList().stream()
// .map(WorkspaceUIListItem::new)
// .collect(Collectors.toList());
// }
//
// public void populateWorkspaceList() {
// workspaceList.setAll(getWorkspaceUIList());
// }
//
// public static void populateWorkspaceConfig(WorkspaceUIListItem
// selectedWorkspace) {
// setWorkspaceConfigInWorkspaceItem(selectedWorkspace);
// prepareWorkspaceJarLibraryFile(selectedWorkspace);
// }
//
// public static void setWorkspaceConfigInWorkspaceItem(WorkspaceUIListItem
// selectedWorkspace) {
// try {
// if (Objects.isNull(selectedWorkspace.getWorkspaceConfig())) {
// selectedWorkspace.setWorkspaceConfig(FileUtil.loadFromFile(
// new File(String.format("%s/%s", selectedWorkspace.getPath(),
// PathConstant.WORKSPACE_JSON)),
// WorkspaceConfig.class));
// }
//
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
//
// public static boolean prepareWorkspaceJarLibraryFile(WorkspaceUIListItem
// selectedWorkspace) {
// WorkspaceConfig workspaceConfig = selectedWorkspace.getWorkspaceConfig();
// String jarPath = workspaceConfig.getLibraryFolderPath() +
// CommonConstant.FileSeparator + PathConstant.JAVA_JAR_FOLDER_PATH;
// Path libraryPath = Paths.get(jarPath);
//// if (!Files.exists(libraryPath)) {
//// log.warn("Library path does not exist: " + libraryPath);
//// return false;
//// }
//// if (!Files.isDirectory(libraryPath)) {
//// log.warn("Library path is not a directory : " + libraryPath);
//// return false;
//// }
//
// String classpath = "";
// try {
// classpath = Files.walk(libraryPath)
// .filter(Files::isRegularFile)
// .filter(p -> p.toString().endsWith(PathConstant.JAR))
// .map(Path::toString)
// .collect(Collectors.joining(File.pathSeparator));
// } catch (IOException e) {
// log.error("Error walking through library directory: " + libraryPath, e);
// return false;
// }
//
// Path classpathFile = Paths.get(jarPath, PathConstant.CLASSPATH_TXT);
// String existingClasspath = "";
//
// if (Files.exists(classpathFile)) {
// try {
// existingClasspath = Files.readString(classpathFile);
// } catch (IOException e) {
// log.error("Error reading existing classpath file: " + classpathFile, e);
// }
// }
// if (!existingClasspath.equals(classpath)) {
// try {
// Files.writeString(classpathFile, classpath);
// } catch (IOException e) {
// log.error("Error writing to classpath file: " + classpathFile, e);
// return false;
// }
// return true;
// }
// return false;
// }
//
//// private void createDataBase(WorkspaceConfig workspaceConfig) {
//// DBUtil.createDatabase(workspaceConfig.getDatabasePath());
//// try {
//// dataSourceConfig.putConnection(workspaceConfig.getDbname(),
//// dataSourceConfig.createDataSource(workspaceConfig.getDatabasePath()).getConnection());
//// } catch (SQLException e) {
//// log.error(e.getMessage());
//// throw new RuntimeException(e);
//// }
//// }
//
//// public ObservableList<WorkspaceUIListItem> populateWorkspaceList() {
//// workspaceList = FXCollections.observableArrayList();
//// List<Map<String, Object>> list = getWorkspaceListByQuery();
//// for (Map<String, Object> row : list) {
//// WorkspaceUIListItem workspace = new WorkspaceUIListItem();
//// workspace.setId((Integer) row.get(CommonConstant.Id)); // Ensure ID is
// stored as String
//// workspace.setWsid((String) row.get(CommonConstant.WsId)); // Ensure ID is
// stored as String
//// workspace.setTitle((String) row.get(CommonConstant.Title));
//// workspace.setPath((String) row.get(CommonConstant.Directory));
//// workspace.setCategory((String) row.get(CommonConstant.Category));
//// workspace.setGroup((String) row.get(CommonConstant.Group));
//// workspace.setDescription((String) row.get(CommonConstant.Description));
//// workspace.setSelected((Integer) row.get(CommonConstant.Selected)); //
// Convert Object to Boolean
//// workspace.setTags((String) row.get(CommonConstant.Tags));
//////
////// // Handle Timestamp conversion
//// // workspace.setCreatedAt(row.get("createdAt") != null ?
// Timestamp.valueOf(row.get("createdAt").toString()) : null);
////// workspace.setUpdatedAt(row.get("updatedAt") != null ?
// Timestamp.valueOf(row.get("updatedAt").toString()) : null);
////
//// workspaceList.add(workspace);
//// }
////
//// return workspaceList;
//// }
//
//// private static void saveConfig(File configFile) {
//// try {
//// FileUtil.saveToFile(this, workspaceJsonFile);
//// } catch (IOException e) {
//// e.printStackTrace();
//// }
//// }
//
//// public static void setExistingConfigFromPath(String workspacePath,
// WorkspaceConfig workspaceConfig) {
//// try {
//// String workspaceJsonPath = String.format("%s/%s", workspacePath,
// PathConstant.WorkspaceJson);
//// workspaceConfig = FileUtil.loadFromFile(new File(workspaceJsonPath),
// WorkspaceConfig.class);
//// workspaceConfig.setWorkspaceFolderPath(workspacePath);
//// workspaceConfig.setWorkspaceJsonPath(workspaceJsonPath);
////
//// } catch (IOException e) {
//// e.printStackTrace();
//// }
//// }
//
// public void copyDirectory(Path source, Path destination) throws IOException {
// Files.walk(source).forEach(sourcePath -> {
// try {
// Path targetPath = destination.resolve(source.relativize(sourcePath));
// if (Files.isDirectory(sourcePath)) {
// Files.createDirectories(targetPath);
// } else {
// Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
// }
// } catch (IOException e) {
// throw new RuntimeException("Error copying workspace", e);
// }
// });
// }
//
// public void createWorkspace(String targetDir, String workspaceName,
// WorkspaceUIListItem workspaceUIListItem) throws IOException {
// Path destinationPath = Paths.get(targetDir, workspaceName);
//
// if (Files.exists(destinationPath)) {
// throw new IOException("Workspace already exists.");
// }
//
// copyDirectory(Paths.get(PathConstant.WORKSPACE_TEMPLATE_PATH_TO_COPY),
// destinationPath);
// updateWorkspaceJson(destinationPath, workspaceName, workspaceUIListItem);
//
// insertWorkspace(workspaceUIListItem);
// }
//
// public void updateSelectedWorkspace(WorkspaceUIListItem workspaceUIListItem)
// {
// updateWorkspace(workspaceUIListItem);
// }
//
// public int insertWorkspace(WorkspaceUIListItem item) {
// return workspaceDao.insertWorkspace(item);
// }
//
// public int updateWorkspace(WorkspaceUIListItem item) {
// return workspaceDao.updateWorkspace(item);
// }
//
// public int deleteWorkspaceByWsId(String wsid) {
// return workspaceDao.deleteWorkspaceByWsId(wsid);
// }
//
// public void removeWorkspace(WorkspaceUIListItem selectedWorkspace) {
// workspaceDao.deleteWorkspaceByWsId(selectedWorkspace.getWsid());
// }
//
// public List<Map<String, Object>> getWorkspaceListByQuery() {
// return workspaceDao.getWorkspaceList();
// }
//
// public static WorkspaceService1 get() {
// return instance;
// }
//
// private void updateWorkspaceJson(Path destinationPath, String workspaceName,
// WorkspaceUIListItem workspaceUIListItem) throws IOException {
// updateWorkspaceJsonPaths(destinationPath, workspaceName,
// workspaceUIListItem);
// }
//
// private void updateWorkspaceJsonPaths(Path destinationPath, String
// workspaceName, WorkspaceUIListItem workspaceUIListItem) throws IOException {
// // Construct the path to the workspace.json file
// Path workspaceJsonPath =
// destinationPath.resolve(PathConstant.WORKSPACE_JSON);
//
// // Check if the workspace.json file exists
// if (!Files.exists(workspaceJsonPath)) {
// throw new IOException("workspace.json not found in the copied directory: " +
// workspaceJsonPath.toAbsolutePath());
// }
//
// // Load the WorkspaceConfig from the JSON file
// WorkspaceConfig workspaceConfig =
// FileUtil.loadFromFile(workspaceJsonPath.toFile(), WorkspaceConfig.class);
//
// // Update the paths
// String newWorkspaceFolderPath = destinationPath.toAbsolutePath().toString();
// workspaceConfig.setId(workspaceUIListItem.getWsid());
// workspaceConfig.setWorkspaceFolderPath(newWorkspaceFolderPath);
// workspaceConfig.setTitle(workspaceName);
// workspaceConfig.setLibraryFolderPath(String.format("%s/library",
// workspaceConfig.getWorkspaceFolderPath()));
// workspaceConfig.setConfigurationFolderPath(String.format("%s/configuration",
// workspaceConfig.getWorkspaceFolderPath()));
// workspaceConfig.setDatabaseFolderPath(String.format("%s/database",
// workspaceConfig.getWorkspaceFolderPath()));
// workspaceConfig.setFilesPath(String.format("%s/files",
// workspaceConfig.getWorkspaceFolderPath()));
// workspaceConfig.setImageFolderPath(String.format("%s/image",
// workspaceConfig.getWorkspaceFolderPath()));
// workspaceConfig.setThemeFolderPath(String.format("%s/theme",
// workspaceConfig.getWorkspaceFolderPath()));
// workspaceConfig.setScriptFolderPath(String.format("%s/script/general",
// workspaceConfig.getWorkspaceFolderPath()));
// workspaceConfig.setWorkspaceJsonPath(String.format("%s/%s",
// workspaceConfig.getWorkspaceFolderPath(), PathConstant.WORKSPACE_JSON));
// workspaceConfig.setDatabasePath(String.format("%s/%s.db",
// workspaceConfig.getDatabaseFolderPath(), workspaceConfig.getDbname()));
//
//
// // Save the updated WorkspaceConfig back to the JSON file
// FileUtil.saveToFile(workspaceConfig, workspaceJsonPath.toFile());
// }
// }
