package in.ripanbaidya.exception_hanlding.exception;

import org.springframework.http.HttpStatus;

/**
 * Error codes for different types of errors.
 * It has fields, business error code, http status and message for error details.
 */
public enum ErrorCode {
    // Validation
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, "Invalid request data"),

    // Auth/Permission
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "Authentication required"),
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "Access is denied"),

    // Not found / conflict / business
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "Requested user not found"),
    RIDER_NOT_FOUND("RIDER_NOT_FOUND", HttpStatus.NOT_FOUND, "Requested rider not found"),
    PAYMENT_FAILED("PAYMENT_FAILED", HttpStatus.UNPROCESSABLE_ENTITY, "Payment processing failed"),
    RIDE_ALREADY_CANCELLED("RIDE_ALREADY_CANCELLED", HttpStatus.CONFLICT, "Ride already cancelled"),
    // Add new business error codes here...

    // Generic server error (you can choose to expose code or not)
    INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

    private final String code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(String code, HttpStatus httpStatus, String defaultMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
