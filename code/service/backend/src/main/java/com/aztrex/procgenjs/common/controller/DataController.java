package com.aztrex.procgenjs.common.controller;

import com.aztrex.procgenjs.common.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;
//
//    @GetMapping("/data")
//    public List<DataItem> getAllData() {
//        return dataService.getAllData();
//    }

    @GetMapping("/data")
    public String getAllData() {
        return "Hello World!";
    }

    /**
     * A more generic endpoint to fetch JSON content from a specified classpath resource path.
     * Example: GET /api/data/resource?path=com/example/config/mydata.json
     *
     * @param filePath The path to the JSON file within the classpath.
     * @return The JSON content or an error response.
     */
    @GetMapping(value = "/resourceAsJsonByPath", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getResourceAsJson(@RequestParam("path") String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\":\"File path parameter 'path' is required.\"}");
        }
        // Basic path validation (you might want to make this more robust)
        if (filePath.contains("..")) {
            return ResponseEntity.badRequest().body("{\"error\":\"Invalid file path.\"}");
        }

        try {
            String jsonContent = dataService.readJsonFromClasspath(filePath);
            return ResponseEntity.ok(jsonContent);
        } catch (IOException e) {
            // Log the error appropriately
            System.err.println("Error reading resource from path '" + filePath + "': " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Resource not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Resource not found at the specified path.\"}");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Could not load resource from the specified path.\"}");
        }
    }

    /**
     * Endpoint to fetch the view configuration JSON for the GT module.
     * Example: GET /api/data/gt/view-configuration
     */
//    @GetMapping(value = "/gt/view-configuration", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> getGtViewConfiguration() {
//        try {
//            // Ensure this path matches the location of your file in src/main/resources
//            String jsonContent = dataService.readJsonFromClasspath("com/aztrex/tracker/modules/pageModules/gt/viewConfiguration.json");
//            return ResponseEntity.ok(jsonContent);
//        } catch (IOException e) {
//            // Log the error appropriately in a real application
//            System.err.println("Error reading GT view configuration: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Could not load view configuration\"}");
//        }
//    }
}