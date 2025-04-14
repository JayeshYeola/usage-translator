package com.example.usagetranslator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Map;

@Slf4j
public class JsonUtil {

    public static Map<String, String> readJson(String jsonFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = JsonUtil.class.getResourceAsStream(jsonFilePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + jsonFilePath);
            }
            return objectMapper.readValue(is, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            log.error("Failed to read or parse JSON mapping from: {}", jsonFilePath, e);
            throw new RuntimeException("Failed to read or parse JSON mapping from: " + jsonFilePath, e);
        }
    }
}
