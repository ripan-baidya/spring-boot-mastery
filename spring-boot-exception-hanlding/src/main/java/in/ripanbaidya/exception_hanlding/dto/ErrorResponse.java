package in.ripanbaidya.exception_hanlding.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * <pre>
 *     {
 *       "success": false,
 *       "status_code": 400,
 *       "error_code": "VALIDATION_ERROR",
 *       "message": "Invalid request data",
 *       "details": [
 *         {
 *           "field": "name",
 *           "error": "Name cannot be empty"
 *         },
 *         {
 *           "field": "age",
 *           "error": "Age must be greater than 18"
 *         }
 *       ],
 *       "timestamp": "2025-10-29 17:42:00",
 *       "path": "/api/user/create"
 *     }
 * </pre>
 */
public class ErrorResponse {
    private boolean success = false;
    private int status_code;
    private String error_code; // optional for generic 500
    private String message;
    private Object details; // either list or object (for flexibility)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;

    private String path;

    // getters / setters / constructors omitted for brevity
    // generate via Lombok or manually

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ErrorResponse() { }

    public ErrorResponse(int status_code, String error_code, String message, Object details, String timestamp, String path) {
        this.status_code = status_code;
        this.error_code = error_code;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
        this.path = path;
    }
}
