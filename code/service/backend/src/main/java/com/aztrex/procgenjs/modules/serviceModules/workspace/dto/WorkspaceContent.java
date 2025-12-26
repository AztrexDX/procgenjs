package com.aztrex.procgenjs.modules.serviceModules.workspace.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkspaceContent {
    private List<String> assets;
    private List<String> databases;
    private List<String> scripts;
    private List<String> javaFiles;
    private List<String> libraries;
}
