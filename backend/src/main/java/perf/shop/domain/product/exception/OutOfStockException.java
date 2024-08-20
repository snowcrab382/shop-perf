package perf.shop.domain.product.exception;

import perf.shop.global.error.exception.BusinessException;
import perf.shop.global.error.exception.ErrorCode;

public class OutOfStockException extends BusinessException {

    public OutOfStockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
