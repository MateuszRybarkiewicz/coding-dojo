package com.assignment.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public final class CommonUtil {
    private CommonUtil() {
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T map(String text, Class<T> clazz) {
        assertFalse("Text is null or blank", StringUtils.isBlank(text));
        assertNotNull("Class is null", clazz);
        try {
            return mapper.readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse text to object: " + clazz, e);
        }
    }

    public static <T> T parseTextToJson(String path, Class<T> clazz) {
        assertFalse("Path is null or blank", StringUtils.isBlank(path));
        assertNotNull("Class is null", clazz);
        ClassPathResource classPathResource = new ClassPathResource(path);
        try (InputStream in = classPathResource.getInputStream()) {
            String text = IOUtils.toString(in, String.valueOf(StandardCharsets.UTF_8));
            return map(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test resource: " + path, e);
        }
    }


    public static JsonNode parseTextToJson(String path) {
        assertFalse("Path is null or blank", StringUtils.isBlank(path));
        return parseTextToJson(path, JsonNode.class);
    }
}
