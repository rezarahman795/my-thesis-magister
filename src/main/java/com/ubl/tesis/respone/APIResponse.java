package com.ubl.tesis.respone;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * APIResponse class for Spring Boot.
 *
 * @param <T> Type of the data in the response.
 */
@Data
public class APIResponse<T> implements Serializable {
    private static final int OK_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final int NOT_FOUND_CODE = 404;
    private static final int BAD_REQUEST_CODE = 400;
    private static final String MESSAGE_SUCCESS = "success";
    private static String SAVED_MESSAGE = "Data NIP terdaftar";

    private int status;
    private boolean success;
    private String message;
    private T data;

    // Default Constructor
    public APIResponse() {
    }

    public APIResponse(int status, boolean success, String message, T data) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Static methods for creating API responses

    public static APIResponse<?> ok() {
        return new APIResponse<>(OK_CODE, true, MESSAGE_SUCCESS,SAVED_MESSAGE);
    }

    public static <T> APIResponse<T> ok(T data) {
        return new APIResponse<>(OK_CODE, true, MESSAGE_SUCCESS, data);
    }

    public static <T> APIResponse<T> save(T data) {
        return new APIResponse<>(OK_CODE, true, SAVED_MESSAGE, null);
    }

    public static <T> APIResponse<T> ok(T data, String message) {
        return new APIResponse<>(OK_CODE, true, message, data);
    }

    public static APIResponse<?> error(String message) {
        return new APIResponse<>(ERROR_CODE, false, message, null);
    }

    public static APIResponse<Void> notFound() {
        return new APIResponse<>(NOT_FOUND_CODE, false, "Data not found", null);
    }

    public static APIResponse<Void> notFoundNIP() {
        return new APIResponse<>(NOT_FOUND_CODE, false, "Data NIP tidak terdaftar", null);
    }

    public static APIResponse<Void> badRequest(String message) {
        return new APIResponse<>(BAD_REQUEST_CODE, false, message, null);
    }

    public static <T> APIResponse<T> create(Map<String, Object> result) {
        // Extract return value
        int outReturn = (Integer) result.get("returnValue");

        // Determine status and success
        int statusCode = outReturn == 1 ? OK_CODE : BAD_REQUEST_CODE;
        boolean success = statusCode == OK_CODE;

        // Extract message and data
        String outMessage = (String) result.get("outMessage");
        T data = (T) result.get("outData");

        // Return APIResponse
        return new APIResponse<>(statusCode, success, outMessage, data);
    }
}
