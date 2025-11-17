package in.ripanbaidya.exception_hanlding.dto;

/**
 * The "details" in Error response is "Object".
 * It can hold validation error like
 *
 * - List<ValidationError> for VALIDATION_ERROR
 * - Map<String,Object> for business-specific details
 */
public class ValidationError {
    private String field;
    private String error;

    public ValidationError() {}

    public ValidationError(String field, String error) {
        this.field = field;
        this.error = error;
    }

    // getters and setters

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
