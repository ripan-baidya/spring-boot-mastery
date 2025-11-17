package in.ripanbaidya.exception_hanlding.exception;

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
