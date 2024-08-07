package perf.shop.domain.auth.exception;

import lombok.Getter;
import perf.shop.global.error.exception.ErrorCode;

@Getter
public class JwtNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public JwtNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
