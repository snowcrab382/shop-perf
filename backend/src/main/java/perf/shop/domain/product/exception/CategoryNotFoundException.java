package perf.shop.domain.product.exception;

import lombok.Getter;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

@Getter
public class CategoryNotFoundException extends EntityNotFoundException {

    private final ErrorCode errorCode;

    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
