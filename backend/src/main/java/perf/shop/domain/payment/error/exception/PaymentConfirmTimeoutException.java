package perf.shop.domain.payment.error.exception;

import perf.shop.global.error.exception.BusinessException;
import perf.shop.global.error.exception.ErrorCode;

public class PaymentConfirmTimeoutException extends BusinessException {

    public PaymentConfirmTimeoutException(ErrorCode errorCode) {
        super(errorCode);
    }
}
