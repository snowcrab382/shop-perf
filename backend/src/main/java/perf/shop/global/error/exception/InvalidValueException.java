package perf.shop.global.error.exception;

public class InvalidValueException extends BusinessException {

    private final ErrorCode errorCode;

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
