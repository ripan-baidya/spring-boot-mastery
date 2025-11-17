package in.ripanbaidya.exception_hanlding.exception;

import java.util.Map;

/**
 * Another example of concrete exception.
 */
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
