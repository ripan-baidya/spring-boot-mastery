package in.ripanbaidya.exception_hanlding.handler;

import in.ripanbaidya.exception_hanlding.dto.ErrorResponse;
import in.ripanbaidya.exception_hanlding.dto.ValidationError;
import in.ripanbaidya.exception_hanlding.exception.BaseException;
import in.ripanbaidya.exception_hanlding.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


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
        body.setTimestamp(String.valueOf(LocalDateTime.now()));
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
        body.setTimestamp(String.valueOf(LocalDateTime.now()));
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
