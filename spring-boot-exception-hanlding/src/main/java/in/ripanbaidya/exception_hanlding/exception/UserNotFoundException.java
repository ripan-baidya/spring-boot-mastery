package in.ripanbaidya.exception_hanlding.exception;

import java.util.Map;

/**
 * Concrete business exceptions
 *
 * Simple, just extend BaseException. Add constructor helpers as needed.
 */
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

    public UserNotFoundException(String message, Map<String, Object> details) {
        super(ErrorCode.USER_NOT_FOUND, message, details);
    }
}
