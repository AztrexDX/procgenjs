package com.aztrex.procgenjs.modules.serviceModules.workspace.model;

import com.aztrex.procgenjs.common.util.constant.CommonConstant;
import com.aztrex.procgenjs.modules.serviceModules.workspace.database.model.WorkspaceRecord;
import com.aztrex.procgenjs.modules.serviceModules.workspace.dto.WorkspaceConfig;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
public class WorkspaceUIListItem {
    private Long id;
    // private String wsid;
    private String title;
    private String details;
    private String path;
    private String category;
    private String group;
    // private Integer selected;
    private String tags;
    private WorkspaceConfig workspaceConfig;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateUpdated;

    // Constructors
    public WorkspaceUIListItem() {
    }

    public WorkspaceUIListItem(WorkspaceRecord workspaceRecord) {
    }

    public WorkspaceUIListItem(String title, String path, String category, String group, String description,
            Integer selected, String tags) {
        // this.id = id;
        this.title = title;
        this.path = path;
        this.category = category;
        this.group = group;
        this.details = description;
        // this.selected = selected;
        this.tags = tags;
    }

    public WorkspaceUIListItem(Map<String, Object> row) {
        this.id = (Long) row.get(CommonConstant.ID); // Ensure ID is stored as String
        // this.wsid = (String) row.get(CommonConstant.WS_ID);
        this.title = (String) row.get(CommonConstant.TITLE);
        this.path = (String) row.get(CommonConstant.DIRECTORY);
        this.category = (String) row.get(CommonConstant.CATEGORY);
        this.group = (String) row.get(CommonConstant.GROUP);
        this.details = (String) row.get(CommonConstant.DETAILS);
        // this.selected = (Integer) row.get(CommonConstant.SELECTED);
        this.tags = (String) row.get(CommonConstant.TAGS);
        // Handle Timestamp conversion
        // this.createdAt = row.get("createdAt") != null ?
        // Timestamp.valueOf(row.get("createdAt").toString()) : null;
        this.dateUpdated = row.get(CommonConstant.DATE_UPDATED) != null
                ? OffsetDateTime.parse(row.get(CommonConstant.DATE_UPDATED).toString())
                : null;
    }

    public WorkspaceRecord toWorkspaceRecord() {
        WorkspaceRecord record = new WorkspaceRecord();
        record.setId(this.getId());
        // record.setWsid(this.getWsid());
        record.setTitle(this.getTitle());
        record.setDetails(this.getDetails());
        record.setPath(this.getPath());
        record.setCategory(this.getCategory());
        record.setGroup(this.getGroup());
        record.setTags(this.getTags());
        record.setDateCreated(this.getDateCreated());
        record.setDateUpdated(this.getDateUpdated());
        record.setWorkspaceConfig(this.getWorkspaceConfig());
        return record;
    }
}
