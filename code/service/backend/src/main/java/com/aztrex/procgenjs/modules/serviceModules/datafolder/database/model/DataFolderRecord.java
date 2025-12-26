package com.aztrex.procgenjs.modules.serviceModules.datafolder.database.model;

import com.aztrex.procgenjs.modules.serviceModules.workspace.dto.WorkspaceConfig;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
public class DataFolderRecord {

    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name = "system-uuid", strategy =
    // "com.aztrex.procgenjs.common.util.generator.CustomStringUUIDGenerator")
    // @Column(name = "id", updatable = false, nullable = false, columnDefinition =
    // "VARCHAR(36)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // @Version
    // private int version;
    @Column(unique = true)
    private String itemId; // Workspace name (e.g., "Project A", "Client X")

    @Column
    private String title; // Path to the workspace folder

    @Column
    private String path;

    @Column(length = 5000)
    private String details;

    @Column
    private String category;

    @Column(name = "\"group\"")
    private String group;

    @Column(length = 1024)
    private String tags;

    @Transient
    private WorkspaceConfig workspaceConfig;

    @Column(nullable = true, updatable = false)
    private OffsetDateTime dateCreated;

    @Column(nullable = true)
    private OffsetDateTime dateUpdated;

    @PrePersist
    public void prePersist() {
        dateCreated = OffsetDateTime.now();
        dateUpdated = dateCreated;
    }

    @PreUpdate
    public void preUpdate() {
        dateUpdated = OffsetDateTime.now();
    }
}