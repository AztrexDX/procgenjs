package com.aztrex.procgenjs.common.utility.classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Getter
public class JsonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper();

/*    // Save configuration
    public static void saveConfig(WorkspaceConfig config, String filePath) throws IOException {
        objectMapper.writeValue(new File(filePath), config);
    }

    // Load configuration
    public static WorkspaceConfig loadConfig(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), WorkspaceConfig.class);
    }*/

    /**
     * Reads a JSON file and returns it as a generic JsonNode.
     * @param filePath The path to file
     * @return The JSON tree
     */
    public static Object getJsonByFilePath(String filePath) throws IOException {
        File configFile = new File(filePath);

        if (!configFile.exists()) {
            throw new RuntimeException("file not found at: " + filePath);
        }

        // readTree parses the file into a generic JSON structure
        return
        objectMapper.convertValue(objectMapper.readTree(configFile), Object.class);
    }
}
