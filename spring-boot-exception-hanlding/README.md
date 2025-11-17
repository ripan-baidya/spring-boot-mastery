# Exception handling in Spring Boot like a Pro


## Design summary (quick)

* `ErrorCode` enum — central mapping: error_code string, `HttpStatus`, default message.
* `BaseException extends RuntimeException` — holds `ErrorCode` and optional `details` map/object.
* Concrete exceptions extend `BaseException` (e.g., `PaymentException`, `UserNotFoundException`).
* `ErrorResponse` DTO — matches your JSON (`success`, `status_code`, `error_code` (optional), `message`, `details`, `timestamp`, `path`).
* `ValidationError` DTO — for field-level validation details.
* `GlobalExceptionHandler` — `@RestControllerAdvice` that maps:

    * `BaseException` → use `ErrorCode`
    * `MethodArgumentNotValidException` / `ConstraintViolationException` → `VALIDATION_ERROR` (400) with field-level details
    * `AccessDeniedException` / `AuthenticationException` → 401/403 mapping
    * Generic `Exception` → 500 (no `error_code` or use `INTERNAL_ERROR`)
* Logging + correlation id + metrics hooks included as comments.

---

## 1) `ErrorCode` enum (central registry)

Create a single source-of-truth enum. Add new codes here when you add business errors.

```java
package com.example.api.error;

import org.springframework.http.HttpStatus;

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
```

> To add a new business error later: add new enum entry (code + HttpStatus + default message). No changes to handler required.

---

## 2) `BaseException` (abstract, extendable)

All business exceptions extend this so handler can treat them uniformly.

```java
package com.example.api.error;

import java.util.Map;

public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details; // optional structured details (could be null)

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    public BaseException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

    public BaseException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
```

---

## 3) Concrete business exceptions

Simple, just extend `BaseException`. Add constructor helpers as needed.

```java
package com.example.api.error;

import java.util.Map;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }

    public UserNotFoundException(Map<String, Object> details) {
        super(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getDefaultMessage(), details);
    }
}
```

```java
package com.example.api.error;

import java.util.Map;

public class RiderNotFoundException extends BaseException {
    public RiderNotFoundException() {
        super(ErrorCode.RIDER_NOT_FOUND);
    }
    public RiderNotFoundException(String message) {
        super(ErrorCode.RIDER_NOT_FOUND, message);
    }
    public RiderNotFoundException(Map<String, Object> details) {
        super(ErrorCode.RIDER_NOT_FOUND, ErrorCode.RIDER_NOT_FOUND.getDefaultMessage(), details);
    }
}
```

```java
package com.example.api.error;

import java.util.Map;

public class PaymentException extends BaseException {
    public PaymentException() {
        super(ErrorCode.PAYMENT_FAILED);
    }
    public PaymentException(String message) {
        super(ErrorCode.PAYMENT_FAILED, message);
    }
    public PaymentException(String message, Map<String, Object> details) {
        super(ErrorCode.PAYMENT_FAILED, message, details);
    }
}
```

---

## 4) Error response DTOs (exact structure you requested)

```java
package com.example.api.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class ErrorResponse {
    private boolean success = false;
    private int status_code;
    private String error_code; // optional for generic 500
    private String message;
    private Object details; // either list or object (for flexibility)
    private String timestamp;
    private String path;

    // getters / setters / constructors omitted for brevity
    // generate via Lombok or manually
}
```

```java
package com.example.api.dto;

public class ValidationError {
    private String field;
    private String error;

    public ValidationError() {}

    public ValidationError(String field, String error) {
        this.field = field;
        this.error = error;
    }

    // getters and setters
}
```

**Note:** `details` is `Object` — it may hold:

* `List<ValidationError>` for `VALIDATION_ERROR`
* `Map<String,Object>` for business-specific details

---

## 5) `GlobalExceptionHandler` — the central mapping

This class maps `BaseException`, Spring validation exceptions, and generic exceptions into the standard JSON.

```java
package com.example.api.handler;

import com.example.api.dto.ErrorResponse;
import com.example.api.dto.ValidationError;
import com.example.api.error.BaseException;
import com.example.api.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final DateTimeFormatter TS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 1) Handle our BaseException (business errors)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        ErrorCode ec = ex.getErrorCode();
        HttpStatus status = ec.getHttpStatus();

        ErrorResponse body = new ErrorResponse();
        body.setSuccess(false);
        body.setStatus_code(status.value());
        body.setError_code(ec.getCode());
        body.setMessage(ex.getMessage() != null ? ex.getMessage() : ec.getDefaultMessage());
        body.setDetails(ex.getDetails());
        body.setTimestamp(ZonedDateTime.now().format(TS_FORMATTER));
        body.setPath(request.getRequestURI());

        // log with level according to status
        if (status.is4xxClientError()) {
            log.info("Business error: {} - {} - path: {}", ec.getCode(), body.getMessage(), body.getPath());
        } else {
            log.error("Server error: {} - {} - path: {}", ec.getCode(), body.getMessage(), body.getPath(), ex);
        }

        return new ResponseEntity<>(body, status);
    }

    // 2) Validation errors (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult br = ex.getBindingResult();

        List<ValidationError> errors = br.getFieldErrors()
                .stream()
                .map(fe -> new ValidationError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse body = new ErrorResponse();
        body.setSuccess(false);
        body.setStatus_code(HttpStatus.BAD_REQUEST.value());
        body.setError_code(ErrorCode.VALIDATION_ERROR.getCode());
        body.setMessage(ErrorCode.VALIDATION_ERROR.getDefaultMessage());
        body.setDetails(errors);
        body.setTimestamp(ZonedDateTime.now().format(TS_FORMATTER));
        body.setPath(request.getRequestURI());

        log.info("Validation failed: {} - path: {}", errors, body.getPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 3) ConstraintViolationException (e.g., @RequestParam validations)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ValidationError> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(msg -> new ValidationError(null, msg))
                .collect(Collectors.toList());

        ErrorResponse body = new ErrorResponse();
        body.setSuccess(false);
        body.setStatus_code(HttpStatus.BAD_REQUEST.value());
        body.setError_code(ErrorCode.VALIDATION_ERROR.getCode());
        body.setMessage(ErrorCode.VALIDATION_ERROR.getDefaultMessage());
        body.setDetails(errors);
        body.setTimestamp(ZonedDateTime.now().format(TS_FORMATTER));
        body.setPath(request.getRequestURI());

        log.info("Constraint violations: {} - path: {}", errors, body.getPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 4) Fallback - unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        // log full stack
        log.error("Unhandled exception at path {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse body = new ErrorResponse();
        body.setSuccess(false);
        body.setStatus_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // optionally set error_code.INTERNAL_ERROR or leave null if you prefer
        body.setError_code(null); // or ErrorCode.INTERNAL_ERROR.getCode()
        body.setMessage("An unexpected error occurred while processing your request");
        body.setDetails(null);
        body.setTimestamp(ZonedDateTime.now().format(TS_FORMATTER));
        body.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
```

**Notes:**

* `MethodArgumentNotValidException` covers `@Valid` request body failures.
* `ConstraintViolationException` covers `@Validated` / `@RequestParam` / `@PathVariable` violations.
* For auth/permission exceptions you can add specific handlers mapping to `UNAUTHORIZED` / `FORBIDDEN` easily.

---

## 6) Controller / Service usage examples

**Controller (validation example)**

```java
@PostMapping("/api/user/create")
public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest req) {
    userService.create(req); // inside service may throw BaseException types
    return ResponseEntity.ok(Map.of("success", true));
}
```

**Service (throwing business exception)**

```java
public UserDto getUser(Long id) {
    return userRepository.findById(id)
          .orElseThrow(() -> new UserNotFoundException());
}
```

**Advanced: throw with details**

```java
Map<String,Object> details = Map.of("userId", id, "reason", "soft-deleted");
throw new UserNotFoundException(details);
```

---

## 7) Unit test / integration testing tips

* Test `GlobalExceptionHandler` by calling controller endpoints with invalid payloads and asserting JSON keys exactly match schema.
* Test `BaseException` mapping by mocking service to throw `PaymentException` and assert `status_code` and `error_code`.

---

## 8) Production considerations / best practices (short)

* **Correlation ID**: Add a request filter that generates `X-Correlation-Id` and include it in logs and the error response (add a `correlation_id` field if you want).
* **Structured logging**: log error_code + path + userContext + correlationId.
* **Metrics**: increment counters for each `ErrorCode` (e.g., Prometheus).
* **Localization**: If supporting locales, `ErrorCode` can map to message keys and messages resolved via `MessageSource`; still return `error_code` for the frontend logic.
* **Don’t leak internals**: For 500 errors, avoid stack traces in response — log them instead.
* **API docs**: Document `ErrorCode` and example responses in your OpenAPI spec.
* **Extensibility**: Add new business errors by adding enum entries + new exception classes (or reuse `BaseException` with specific ErrorCode without new class).
* **Mapping JSON schema**: Keep `details` generic so you can return validation arrays or business-specific maps without breaking clients.

---

## 9) Example JSON responses (exact shapes you required)

**Validation error example:**

```json
{
  "success": false,
  "status_code": 400,
  "error_code": "VALIDATION_ERROR",
  "message": "Invalid request data",
  "details": [
    {
      "field": "name",
      "error": "Name cannot be empty"
    },
    {
      "field": "age",
      "error": "Age must be greater than 18"
    }
  ],
  "timestamp": "2025-10-29 17:42:00",
  "path": "/api/user/create"
}
```

**Business exception example (user not found):**

```json
{
  "success": false,
  "status_code": 404,
  "error_code": "USER_NOT_FOUND",
  "message": "Requested user not found",
  "details": {
    "userId": 123
  },
  "timestamp": "2025-11-02 12:00:00",
  "path": "/api/user/123"
}
```

**Generic server error (500):**

```json
{
  "success": false,
  "status_code": 500,
  "error_code": null,
  "message": "An unexpected error occurred while processing your request",
  "details": null,
  "timestamp": "2025-11-02 12:00:00",
  "path": "/api/rider/payments"
}
```

(If you prefer to include `error_code: "INTERNAL_ERROR"` for 500, that’s fine — just be consistent.)

---

## 10) How to add a *new* business exception (summary)

1. Add an `ErrorCode` enum constant (code + HttpStatus + default message).
2. Create a new exception class extending `BaseException` (optional — you can throw new `BaseException(ErrorCode.X)` directly).
3. Throw it from service layer.
4. `GlobalExceptionHandler` will automatically convert it to the proper JSON.



