package com.framework.utils;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * ApiUtils - RestAssured wrapper for REST API testing.
 * Provides a fluent builder pattern for building and executing API requests.
 */
public class ApiUtils {

    private static final Logger log = LogManager.getLogger(ApiUtils.class);
    private static final String BASE_URI = ConfigManager.getInstance().getApiBaseUrl();

    private ApiUtils() {}

    /**
     * Returns a base RequestSpecification pre-configured with base URI and JSON content type.
     */
    public static RequestSpecification given() {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().ifValidationFails();
    }

    public static RequestSpecification given(String baseUri) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().ifValidationFails();
    }

    // ─── HTTP Methods ──────────────────────────────────────────────────────────

    public static Response get(String endpoint) {
        log.info("GET {}{}", BASE_URI, endpoint);
        Response response = given().get(endpoint);
        log.info("Response Status: {}", response.getStatusCode());
        return response;
    }

    public static Response get(String endpoint, Map<String, ?> queryParams) {
        log.info("GET {}{} | Params: {}", BASE_URI, endpoint, queryParams);
        return given().queryParams(queryParams).get(endpoint);
    }

    public static Response get(String endpoint, Map<String, String> headers, Map<String, ?> queryParams) {
        return given().headers(headers).queryParams(queryParams).get(endpoint);
    }

    public static Response post(String endpoint, Object body) {
        log.info("POST {}{}", BASE_URI, endpoint);
        return given().body(body).post(endpoint);
    }

    public static Response post(String endpoint, Object body, Map<String, String> headers) {
        return given().headers(headers).body(body).post(endpoint);
    }

    public static Response put(String endpoint, Object body) {
        log.info("PUT {}{}", BASE_URI, endpoint);
        return given().body(body).put(endpoint);
    }

    public static Response patch(String endpoint, Object body) {
        log.info("PATCH {}{}", BASE_URI, endpoint);
        return given().body(body).patch(endpoint);
    }

    public static Response delete(String endpoint) {
        log.info("DELETE {}{}", BASE_URI, endpoint);
        return given().delete(endpoint);
    }

    // ─── Response Helpers ──────────────────────────────────────────────────────

    public static void validateStatusCode(Response response, int expectedCode) {
        int actual = response.getStatusCode();
        if (actual != expectedCode) {
            throw new AssertionError("Expected status " + expectedCode + " but got " + actual
                + "\nBody: " + response.getBody().asString());
        }
        log.info("Status code validated: {}", expectedCode);
    }

    public static String extractValue(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    public static <T> T extractValue(Response response, String jsonPath, Class<T> clazz) {
        return response.jsonPath().getObject(jsonPath, clazz);
    }

    public static String getResponseBody(Response response) {
        return response.getBody().asPrettyString();
    }

    // ─── Auth Helpers ──────────────────────────────────────────────────────────

    public static RequestSpecification withBearerToken(String token) {
        return given().header("Authorization", "Bearer " + token);
    }

    public static RequestSpecification withBasicAuth(String username, String password) {
        return given().auth().basic(username, password);
    }
}
