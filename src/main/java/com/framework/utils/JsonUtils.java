package com.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * JsonUtils - Jackson-powered JSON utility for reading test data files
 * and serializing/deserializing POJOs.
 */
public class JsonUtils {

    private static final Logger log = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper MAPPER = createMapper();

    private JsonUtils() {}

    private static ObjectMapper createMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // ─── Read from File ────────────────────────────────────────────────────────

    public static <T> T readFromFile(String filePath, Class<T> clazz) {
        try {
            log.debug("Reading JSON file as {}: {}", clazz.getSimpleName(), filePath);
            return MAPPER.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    public static <T> T readFromFile(String filePath, TypeReference<T> typeRef) {
        try {
            return MAPPER.readValue(new File(filePath), typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    public static List<Map<String, Object>> readAsListOfMaps(String filePath) {
        return readFromFile(filePath, new TypeReference<>() {});
    }

    public static Map<String, Object> readAsMap(String filePath) {
        return readFromFile(filePath, new TypeReference<>() {});
    }

    // ─── Read from Classpath ───────────────────────────────────────────────────

    public static <T> T readFromClasspath(String resourcePath, Class<T> clazz) {
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new RuntimeException("Resource not found: " + resourcePath);
            return MAPPER.readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read classpath resource: " + resourcePath, e);
        }
    }

    public static Map<String, Object> readFromClasspathAsMap(String resourcePath) {
        return readFromClasspath(resourcePath, new TypeReference<>() {});
    }

    private static <T> T readFromClasspath(String resourcePath, TypeReference<T> typeRef) {
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new RuntimeException("Resource not found: " + resourcePath);
            return MAPPER.readValue(is, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read classpath resource: " + resourcePath, e);
        }
    }

    // ─── Read from String ──────────────────────────────────────────────────────

    public static <T> T readFromString(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
    }

    public static Map<String, Object> readStringAsMap(String json) {
        return readFromString(json, new TypeReference<>() {});
    }

    private static <T> T readFromString(String json, TypeReference<T> typeRef) {
        try {
            return MAPPER.readValue(json, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
    }

    // ─── JsonNode access ───────────────────────────────────────────────────────

    public static JsonNode readAsJsonNode(String filePath) {
        try {
            return MAPPER.readTree(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON node from file: " + filePath, e);
        }
    }

    public static String getJsonValue(String filePath, String fieldName) {
        JsonNode node = readAsJsonNode(filePath);
        JsonNode field = node.get(fieldName);
        if (field == null) throw new RuntimeException("Field not found in JSON: " + fieldName);
        return field.asText();
    }

    // ─── Write ─────────────────────────────────────────────────────────────────

    public static void writeToFile(Object object, String filePath) {
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), object);
            log.info("JSON written to: {}", filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON to file: " + filePath, e);
        }
    }

    public static String toJsonString(Object object) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialise object to JSON", e);
        }
    }

    // ─── Conversion ────────────────────────────────────────────────────────────

    public static <T> T convertValue(Object fromValue, Class<T> toClass) {
        return MAPPER.convertValue(fromValue, toClass);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> typeRef) {
        return MAPPER.convertValue(fromValue, typeRef);
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
