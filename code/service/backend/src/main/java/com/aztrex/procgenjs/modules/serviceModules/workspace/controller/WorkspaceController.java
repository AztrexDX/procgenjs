package com.aztrex.procgenjs.modules.serviceModules.workspace.controller;

import com.aztrex.procgenjs.common.utility.classes.JsonUtil;
import com.aztrex.procgenjs.common.utility.constant.PathConstant;
import com.aztrex.procgenjs.modules.serviceModules.workspace.database.model.WorkspaceRecord;
import com.aztrex.procgenjs.modules.serviceModules.workspace.dto.model.WorkspaceUIListItem;
import com.aztrex.procgenjs.modules.serviceModules.workspace.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    @Autowired
    @Lazy
    private WorkspaceService service;

    @PostMapping(value = "/add")
    public WorkspaceRecord addEntry(@RequestBody WorkspaceRecord entry) throws IOException {
        // service.createWorkspace(entry.getPath(), entry.getId());
        return service.addEntry(entry);
    }

    @PostMapping(value = "/create")
    public WorkspaceRecord createEntry(@RequestBody WorkspaceRecord entry) throws IOException {
        service.createWorkspace(entry.getPath(), entry.getTitle());
        return service.addEntry(entry);
    }

    @PutMapping(value = "/update")
    public WorkspaceRecord updateEntry(@RequestBody WorkspaceRecord entry) {
        return service.updateEntry(entry.getId(), entry);
    }

    @PutMapping("/updateWorkspaceUIListItem")
    public ResponseEntity<WorkspaceRecord> updateSelectedWorkspace(
            @RequestBody WorkspaceUIListItem workspaceUIListItem) {
        WorkspaceRecord record = service.updateWorkspace(workspaceUIListItem);
        return ResponseEntity.ok(record);
    }

    @PostMapping(value = "/addorupdate")
    public WorkspaceRecord addOrUpdateEntry(@RequestBody WorkspaceRecord entry) {
        return service.addOrUpdate(entry);
    }

    @PutMapping(value = "/updateById")
    public WorkspaceRecord updateEntry(@RequestParam(value = "id") Long id, @RequestBody WorkspaceRecord entry) {
        return service.updateEntry(id, entry);
    }

    // @RequestMapping(value="/queryRecord", method=RequestMethod.POST)
    // public void queryRecord() {
    // service.queryRecord();
    // }

    @GetMapping(value = "/get")
    public WorkspaceRecord getEntry(@RequestParam(value = "id") Long id) {
        return service.getById(id);
    }

    @GetMapping(value = "/getAll")
    public List<WorkspaceRecord> getAll() {
        return service.getAll();
    }

    @GetMapping("/getAllWorkspaceUIList")
    public List<WorkspaceUIListItem> getAllWorkspaceUIList() {
        return service.getWorkspaceUIList();
    }

    @GetMapping(value = "/getAllByPage")
    public ResponseEntity<Map<String, Object>> getAllBypage(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sort) {
        ResponseEntity<Map<String, Object>> response = service.getAll(page, size, sort);

        return response;
    }

    @GetMapping(value = "/getWorkspaceConfigById")
    public Object getWorkspaceConfigById(@RequestParam(value = "id") Long id) throws IOException {
        WorkspaceRecord record = service.getById(id);
        return JsonUtil.getJsonByFilePath(record.getPath() + File.separator + record.getTitle()
                + File.separator + PathConstant.WORKSPACE_JSON);
    }

    @GetMapping(value = "/getWorkspaceConfigByItemId")
    public Object getWorkspaceConfigByItemId(@RequestParam(value = "itemId") String itemId) throws IOException {
        WorkspaceRecord record = service.getByItemId(itemId);
        return JsonUtil.getJsonByFilePath(record.getPath() + File.separator + record.getTitle()
                + File.separator + PathConstant.WORKSPACE_JSON);
    }


    // @RequestMapping(value="/getProjectsIdAndName", method=RequestMethod.GET)
    // public List<ProjectNameAndId> findProjectsIdAndName() {
    // return service.findProjectIdAndName();
    // }
    //
    // @RequestMapping(value="/getProjectsIdPidAndName", method=RequestMethod.GET)
    // public List<ProjectNameAndId> findProjectsIdPidAndName() {
    // return service.findProjectIdPidAndName();
    // }

    /*
     * @RequestMapping(value="/getAllByParameter", method=RequestMethod.GET)
     * public ResponseEntity<Map<String, Object>> getAllByParameter(
     * 
     * @RequestParam(defaultValue = "") UUID id,
     * 
     * @RequestParam(defaultValue = "") String name,
     * 
     * @RequestParam(defaultValue = "") String used,
     * 
     * @RequestParam(defaultValue = "") String details,
     * 
     * @RequestParam(defaultValue = "") String notes,
     * 
     * @RequestParam(defaultValue = "") String type,
     * 
     * @RequestParam(defaultValue = "") String category,
     * 
     * @RequestParam(defaultValue = "") String subcategory,
     * 
     * @RequestParam(defaultValue = "") String group,
     * 
     * @RequestParam(defaultValue = "") String subgroup,
     * 
     * @RequestParam(defaultValue = "") String section,
     * 
     * @RequestParam(defaultValue = "") String tags,
     * 
     * @RequestParam(defaultValue = "") String links,
     * 
     * @RequestParam(defaultValue = "") @DateTimeFormat(iso =
     * DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dateCreated,
     * 
     * @RequestParam(defaultValue = "") @DateTimeFormat(iso =
     * DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dateUpdated,
     * 
     * @RequestParam(defaultValue = "0") Integer page,
     * 
     * @RequestParam(defaultValue = "10") Integer size,
     * 
     * @RequestParam(defaultValue = "name") String sort) {
     * ResponseEntity<Map<String, Object>> response = service.getAllByParameter(id,
     * name, used, details, notes, type, category, subcategory, group, subgroup,
     * section, tags, links, dateCreated, dateUpdated, page, size, sort);
     * 
     * return response;
     * }
     */

    @GetMapping(value = "/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam Map<String, String> searchParams,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sort) {
        ResponseEntity<Map<String, Object>> response = service.search(searchParams, page, size, sort);

        return response;
    }

    // @RequestMapping(value="/getAllByCollectionIds/{ids}",
    // method=RequestMethod.GET)
    // public List<WorkspaceRecord> getAllByCollectionIds(@PathVariable(value =
    // "ids") Set<String> ids) {
    // return service.getByCollectionIds(ids);
    // }

    @DeleteMapping(value = "/deleteEntry")
    public ResponseEntity<HttpStatus> deleteEntry(WorkspaceRecord entry) {
        return service.deleteEntry(entry);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<HttpStatus> deleteById(@RequestParam(value = "id") Long id) {
        return service.deleteById(id);
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteAll() {
        return service.deleteAll();
    }

    // @PostMapping("/create")
    // public ResponseEntity<?> createWorkspace(@RequestParam String targetDir,
    // @RequestParam String folderName
    // ) {
    // try {
    // service.createWorkspace(targetDir, folderName);
    // return ResponseEntity.ok().body("Workspace created successfully");
    // } catch (IOException e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed
    // to create workspace: " + e.getMessage());
    // }
    // }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<HttpStatus> removeSelectedWorkspace(@PathVariable Long id) {
        service.removeWorkspace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/chooseTargetDirectory")
    public ResponseEntity<String> chooseTargetDirectory(
            @RequestParam String targetDirectory) {
        return ResponseEntity.ok().body(
                service.chooseTargetDirectory(targetDirectory));
    }

//    @GetMapping("/content/{id}")
//    public ResponseEntity<WorkspaceContent> getWorkspaceContent(@PathVariable Long id) {
//        return ResponseEntity.ok(service.getWorkspaceContent(id));
//    }

}
