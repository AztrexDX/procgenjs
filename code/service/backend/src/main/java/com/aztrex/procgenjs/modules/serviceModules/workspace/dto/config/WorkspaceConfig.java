package com.aztrex.procgenjs.modules.serviceModules.workspace.dto.config;

import com.aztrex.procgenjs.common.utility.constant.CommonConstant;
import com.aztrex.procgenjs.modules.serviceModules.workspace.dto.model.WorkspaceNamePathItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Configuration
public class WorkspaceConfig implements Serializable {
    // private DataSourceConfig dataSourceConfig = new DataSourceConfig();

    private String id = UUID.randomUUID().toString();
    private String title = "workspace";
    private String dbname = "tracker";
    private String configurationFolderPath;
    private String databaseFolderPath;
    private String databasePath;
    private String filesPath;
    private String imageFolderPath;
    private String themeFolderPath;
    private String scriptFolderPath;
    private String libraryFolderPath;
    private String workspaceFolderPath = String.format("%s/%s", "./", title);
    private String workspaceJsonPath = String.format("%s%s%s", workspaceFolderPath, title,
            CommonConstant.JSON_EXTENSION);

    private List<WorkspaceNamePathItem> workspaceNamePathItems = new ArrayList<>();
    // private WorkspaceUISettings workspaceUISettings = new WorkspaceUISettings();

    // private Map<String, Connection> dbConnections = new HashMap<>();

    public void initialize() {
        setUp();
    }

    private void setUp() {
        setUpPaths();
        // setUpWorkspaceNamePathItem();
    }

    private void setUpPaths() {
        configurationFolderPath = String.format("%s/configuration", workspaceFolderPath);
        libraryFolderPath = String.format("%s/library", workspaceFolderPath);
        databaseFolderPath = String.format("%s/database", workspaceFolderPath);
        databasePath = String.format("%s/%s.db", databaseFolderPath, dbname);
        filesPath = String.format("%s/files", workspaceFolderPath);
        imageFolderPath = String.format("%s/image", workspaceFolderPath);
        themeFolderPath = String.format("%s/theme", workspaceFolderPath);
        scriptFolderPath = String.format("%s/script/general", workspaceFolderPath);
        workspaceJsonPath = String.format("%s/%s", workspaceFolderPath, "workspace.json");
    }

    private void setUpWorkspaceNamePathItem() {
        WorkspaceNamePathItem item = new WorkspaceNamePathItem();
        item.setTitle(title);
        item.setPath(workspaceJsonPath);
        workspaceNamePathItems.add(item);
    }

    public void setTitlePath(String title, String workspaceFolderPath) {
        this.setTitle(title);
        // this.setDbname(dbname);
        this.setWorkspaceFolderPath(workspaceFolderPath);
        // dbConnections.put(dbname, null);
    }

    public void setTitleDbPath(String title, String dbname, String workspaceFolderPath) {
        this.setTitle(title);
        this.setDbname(dbname);
        this.setWorkspaceFolderPath(workspaceFolderPath);
        // dbConnections.put(dbname, null);
    }

}
