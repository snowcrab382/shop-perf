package perf.shop.domain.product.exception;

import lombok.Getter;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@Getter
public class ProductNotFoundException extends EntityNotFoundException {

    private final ErrorCode errorCode;

    public ProductNotFoundException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
