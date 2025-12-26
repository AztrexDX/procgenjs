package com.aztrex.procgenjs.common.service;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {


    /**
     * Reads a file from the classpath and returns its content as a String.
     * This method is suitable for reading JSON files or any text-based files.
     *
     * @param filePath The path to the file within the classpath (e.g., "com/aztrex/tracker/modules/pageModules/gt/viewConfiguration.json").
     * @return The content of the file as a String.
     * @throws IOException If the file cannot be found or read.
     */
    public String readJsonFromClasspath(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        if (!resource.exists()) {
            throw new IOException("Resource not found at path: " + filePath);
        }
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            return new String(bdata, StandardCharsets.UTF_8);
        }
    }

}