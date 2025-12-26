package com.aztrex.procgenjs.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

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
}
