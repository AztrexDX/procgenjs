package com.aztrex.procgenjs.modules.serviceModules.datafolder.dto;

import com.aztrex.procgenjs.common.util.constant.CommonConstant;
import com.aztrex.procgenjs.modules.serviceModules.workspace.model.WorkspaceNamePathItem;
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
public class DataFolderConfig implements Serializable {
    // private DataSourceConfig dataSourceConfig = new DataSourceConfig();

    private String id = UUID.randomUUID().toString();
    private String title = "dataFolder";
    private String dbname = "procgenjs";
    private String configurationFolderPath;
    private String databaseFolderPath;
    private String databasePath;
    private String filesPath;
    private String assetsFolderPath;
//    private String scriptFolderPath;
//    private String libraryFolderPath;
    private String dataFolderPath = String.format("%s/%s", "./", title);
    private String dataFolderJsonPath = String.format("%s%s%s", dataFolderPath, title,
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
        configurationFolderPath = String.format("%s/configuration", dataFolderPath);
//        libraryFolderPath = String.format("%s/library", dataFolderPath);
        databaseFolderPath = String.format("%s/database", dataFolderPath);
        databasePath = String.format("%s/%s.db", databaseFolderPath, dbname);
        filesPath = String.format("%s/files", dataFolderPath);
        assetsFolderPath = String.format("%s/assets", dataFolderPath);
//        scriptFolderPath = String.format("%s/script/general", workspaceFolderPath);
        dataFolderJsonPath = String.format("%s/%s", dataFolderPath, "dataFolder.json");
    }

    private void setUpWorkspaceNamePathItem() {
        WorkspaceNamePathItem item = new WorkspaceNamePathItem();
        item.setTitle(title);
        item.setPath(dataFolderJsonPath);
        workspaceNamePathItems.add(item);
    }

    public void setTitlePath(String title, String workspaceFolderPath) {
        this.setTitle(title);
        // this.setDbname(dbname);
        this.setDataFolderPath(workspaceFolderPath);
        // dbConnections.put(dbname, null);
    }

    public void setTitleDbPath(String title, String dbname, String workspaceFolderPath) {
        this.setTitle(title);
        this.setDbname(dbname);
        this.setDataFolderPath(workspaceFolderPath);
        // dbConnections.put(dbname, null);
    }

}
