package perf.shop.global.error.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends BusinessException {

    private final ErrorCode errorCode;

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
