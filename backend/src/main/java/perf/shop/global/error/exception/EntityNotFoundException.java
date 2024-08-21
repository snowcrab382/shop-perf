package perf.shop.global.error.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
