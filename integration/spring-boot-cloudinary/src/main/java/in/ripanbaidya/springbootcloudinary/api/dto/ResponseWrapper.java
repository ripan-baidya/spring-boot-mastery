package in.ripanbaidya.springbootcloudinary.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Standard API responses wrapper.
 * <p>This class standardizes the JSON response structure across the application. It includes support for
 * status codes, messages, data payloads, timestamps, and request paths.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        {
                "success",
                "message",
                "timestamp",
                "data"
        }
)
public class ResponseWrapper<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String message;
    private T data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Private constructor for the Builder.
    private ResponseWrapper(Builder<T> builder) {
        this.success = builder.success;
        this.message = builder.message;
        this.data = builder.data;
        this.timestamp = builder.timestamp != null ? builder.timestamp : LocalDateTime.now();
    }

    // ==================== Static Factory Methods ====================

    public static <T> ResponseWrapper<T> success(T data, String message) {
        return new Builder<T>()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }


    public static <T> ResponseWrapper<T> error(String message, HttpStatus status) {
        return new Builder<T>()
                .success(false)
                .message(message)
                .build();
    }


    // ==================== Getters & Setters ====================

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // ==================== Builder Pattern ====================

    public static class Builder<T> {
        private Boolean success;
        private String message;
        private T data;
        private LocalDateTime timestamp;

        public Builder() {
        }

        public Builder<T> success(Boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ResponseWrapper<T> build() {
            return new ResponseWrapper<>(this);
        }
    }
}