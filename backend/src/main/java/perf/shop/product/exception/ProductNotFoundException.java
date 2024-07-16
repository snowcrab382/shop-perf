package perf.shop.product.exception;

import perf.shop.global.error.exception.BusinessException;
import perf.shop.global.error.exception.ErrorCode;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
