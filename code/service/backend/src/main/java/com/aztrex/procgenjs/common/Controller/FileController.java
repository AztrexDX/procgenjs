package com.aztrex.procgenjs.common.Controller;

import com.aztrex.procgenjs.common.utility.classes.JsonUtil;
import com.aztrex.procgenjs.modules.serviceModules.workspace.database.model.WorkspaceRecord;
import com.aztrex.procgenjs.modules.serviceModules.workspace.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {


    @GetMapping(value = "/getJsonByFilePath")
    public Object getEntry(@RequestParam(value = "filePath") String filePath) throws IOException {
        return JsonUtil.getJsonByFilePath(filePath);
    }

    }