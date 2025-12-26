package com.aztrex.procgenjs.modules.serviceModules.workspace.database.dao;// package com.aztrex.common.modules.workspace.database.dao;
//
// import com.zaxxer.hikari.HikariDataSource;
// import lombok.Getter;
// import lombok.Setter;
// import org.springframework.jdbc.core.JdbcTemplate;
//
// import java.sql.PreparedStatement;
// import java.sql.SQLException;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;
//
// @Getter
// @Setter
// public class WorkspaceDao {
// private AppState appState;
// private static WorkspaceDao instance;
// private DatabaseService databaseService;
// private JdbcTemplateMap jdbcTemplateMap;
// private JdbcTemplateQuery jdbcTemplateQuery;
// private JdbcTemplate procgenfxJdbcTemplate;
// public static HikariDataSource procgenfxDataSource;
//
// public WorkspaceDao() {
// instance = this;
// }
//
// public void initialize() {
// initializeService();
// }
//
// private void initializeService() {
// jdbcTemplateQuery = JdbcTemplateQuery.get();
// }
//
// public void setUpPostConstruct() {
// databaseService = DatabaseService.get();
// procgenfxJdbcTemplate = DatabaseService.procgenfxJdbcTemplate;
// }
//
// public void saveWorkspace() {
//
// }
//
// public void insertWorkspace(String title, String directory, String category,
// String group,
// String description, boolean selected, String tags) {
//
// try (PreparedStatement stmt =
// procgenfxDataSource.getConnection().prepareStatement(WorkspaceQuery.InsertQuery,
// PreparedStatement.RETURN_GENERATED_KEYS)) {
// String wsid = UUID.randomUUID().toString(); // Generate a UUID for wsid
//
// stmt.setString(1, wsid);
// stmt.setString(2, title != null ? title : "title"); // Default to "title"
// stmt.setString(3, directory != null ? directory : "directory"); // Default to
// "directory"
// stmt.setString(4, category);
// stmt.setString(5, group);
// stmt.setString(6, description);
// stmt.setBoolean(7, selected);
// stmt.setString(8, tags);
//
// int affectedRows = stmt.executeUpdate();
//
//// if (affectedRows > 0) {
//// try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//// if (generatedKeys.next()) {
//// return generatedKeys.getInt(1); // Return the generated ID
//// }
//// }
//// }
// } catch (SQLException e) {
// e.printStackTrace();
// throw new RuntimeException(e);
// }
//
//// return -1; // Return -1 if insertion fails
// }
////
//// public int insertWorkspace(WorkspaceUIListItem item) {
//// return jdbcTemplateQuery.executeUpdateQueryByJdbcTemplateId(
//// JdbcTemplateQuery.procgenfxJdbcTemplate, CommonConstant.ProcgenfxData,
//// WorkspaceQuery.InsertQuery, item.getWsid(),
//// item.getTitle(),
//// item.getPath(),
//// item.getCategory(),
//// item.getGroup(),
//// item.getDescription(),
//// item.getSelected(),
//// item.getTags()
//// );
//// }
////
//// public int updateWorkspace(WorkspaceUIListItem item) {
//// return jdbcTemplateQuery.executeUpdateQueryByJdbcTemplateId(
//// JdbcTemplateQuery.procgenfxJdbcTemplate, CommonConstant.ProcgenfxData,
//// WorkspaceQuery.UpdateQuery, item.getWsid(),
//// item.getTitle(),
//// item.getPath(),
//// item.getCategory(),
//// item.getGroup(),
//// item.getDescription(),
//// item.getSelected(),
//// item.getTags(),
//// item.getId()
//// );
//// }
////
//// public int deleteWorkspaceByWsId(String WsId) {
//// return jdbcTemplateQuery.executeUpdateQueryByJdbcTemplateId(
//// JdbcTemplateQuery.procgenfxJdbcTemplate, CommonConstant.ProcgenfxData,
//// WorkspaceQuery.DeleteQueryByWsId, WsId
//// );
//// }
////
//// public List<Map<String, Object>> getWorkspaceList() {
//// return
// jdbcTemplateQuery.executeSelectQueryByJdbcTemplateId(procgenfxJdbcTemplate,
// CommonConstant.ProcgenfxData,
//// WorkspaceQuery.SelectQuery);
//// }
////
//// public int insertWorkspace(JdbcTemplate jdbcTemplate, WorkspaceUIListItem
// item) {
//// return jdbcTemplate.update(WorkspaceQuery.InsertQuery,
//// item.getId(),
//// item.getTitle(),
//// item.getPath(),
//// item.getCategory(),
//// item.getGroup(),
//// item.getDescription(),
//// item.getSelected(),
//// item.getTags()
//// );
//// }
//
// public static WorkspaceDao get() {
// return instance;
// }
//
// }
