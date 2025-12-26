package com.aztrex.procgenjs.modules.serviceModules.workspace.database;// package com.aztrex.common.modules.workspace.database;
//
// import com.aztrex.procgenjs.common.database.RoutingDataSource;
// import com.aztrex.procgenjs.common.util.FileUtil;
// import com.aztrex.procgenjs.common.util.constant.PathConstant;
// import
// com.aztrex.common.modules.workspace.dto.WorkspaceConfig;
// import
// com.aztrex.common.modules.workspace.database.model.WorkspaceRecord;
// import
// com.aztrex.common.modules.workspace.service.WorkspaceService;
// import jakarta.annotation.PostConstruct;
// import lombok.Data;
// import lombok.Getter;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.jdbc.datasource.DriverManagerDataSource;
// import org.springframework.stereotype.Component;
//
// import javax.sql.DataSource;
// import java.io.File;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Objects;
//
// @Data
// @Component
// public class WorkspaceDataSourceRegistry {
//
// private final Map<String, DataSource> dataSources = new HashMap<>();
//
// @Autowired
// private RoutingDataSource routingDataSource;
//
// @Autowired
// private WorkspaceService workspaceService;
//
// @Value("${app.database.path}")
// private String databasePath;
//
// @PostConstruct
// private void initializePostConstruct() {
//
// }
//
// public Map<String, DataSource> getDataSources() {
// return dataSources;
// }
//
// public DataSource getDataSource(String workspaceId) {
// return dataSources.computeIfAbsent(workspaceId,
// this::createAndRegisterDataSource);
// }
//
// private DataSource createAndRegisterDataSource(String workspaceId) {
// WorkspaceRecord workspaceRecord = workspaceService.getById(workspaceId);
// if (Objects.isNull(workspaceRecord.getWorkspaceConfig())) {
// try {
// workspaceRecord.setWorkspaceConfig(FileUtil.loadFromFile(
// new File(String.format("%s/%s", workspaceRecord.getPath(),
// PathConstant.WORKSPACE_JSON)),
// WorkspaceConfig.class));
// } catch (IOException e) {
// throw new RuntimeException(e);
// }
// }
//
// String workspaceDatabasePath =
// workspaceRecord.getWorkspaceConfig().getDatabasePath();
// // Replace with your logic to get connection details from a config file,
// database, etc.
// // For example, you might have a WorkspaceConfig object that stores these
// details.
// // For this example, we'll just create a new in-memory H2 database for each
// workspace.
// DriverManagerDataSource dataSource = new DriverManagerDataSource();
// dataSource.setDriverClassName("org.sqlite.JDBC");
// dataSource.setUrl("jdbc:sqlite:" + workspaceDatabasePath);
//// dataSource.setUsername("sa");
//// dataSource.setPassword("");
//
// // Register the new DataSource with the RoutingDataSource
// registerDataSource(workspaceId, dataSource);
//
// return dataSource;
// }
//
// private synchronized void registerDataSource(String workspaceId, DataSource
// dataSource) {
// dataSources.put(workspaceId, dataSource);
// Map<Object, Object> targetDataSources = new HashMap<>(dataSources);
// routingDataSource.setTargetDataSources(targetDataSources);
// routingDataSource.afterPropertiesSet(); // Re-initialize the
// RoutingDataSource
// }
// }