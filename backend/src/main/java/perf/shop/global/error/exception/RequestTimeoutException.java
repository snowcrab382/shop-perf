package perf.shop.global.error.exception;

public class RequestTimeoutException extends BusinessException {

    public RequestTimeoutException(ErrorCode errorCode) {
        super(errorCode);
    }
}
