package com.aztrex.procgenjs.common.util.converter;

import java.io.IOException;
import java.util.Map;

import jakarta.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {
	private final ObjectMapper objectMapper = new ObjectMapper();
	
    public String convertToDatabaseColumn(Map<String, Object> data) {
 
        String customerInfoJson = null;
        try {
            customerInfoJson = objectMapper.writeValueAsString(data);
        } catch (final JsonProcessingException e) {
           // logger.error("JSON writing error", e);
        }
 
        return customerInfoJson;
    }
 
    public Map<String, Object> convertToEntityAttribute(String data) {
 
        Map<String, Object> customerInfo = null;
        try {
            customerInfo = objectMapper.readValue(data, Map.class);
        } catch (final IOException e) {
          //  logger.error("JSON reading error", e);
        }
 
        return customerInfo;
    }
 
}
