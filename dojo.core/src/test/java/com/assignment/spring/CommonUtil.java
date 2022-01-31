package com.assignment.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class CommonUtil {
    private CommonUtil() {
    }

    public static <T> T parseTextToJson(String path, Class<T> clazz) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        try (InputStream in = classPathResource.getInputStream()) {
            String text = IOUtils.toString(in, String.valueOf(StandardCharsets.UTF_8));
            return new ObjectMapper().readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test resource: " + path, e);
        }
    }

    public static JsonNode parseTextToJson(String path) {
        return parseTextToJson(path, JsonNode.class);
    }
}
