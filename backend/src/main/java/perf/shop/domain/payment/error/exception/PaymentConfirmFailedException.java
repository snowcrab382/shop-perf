package perf.shop.domain.payment.error.exception;

import perf.shop.global.error.exception.BusinessException;

public class PaymentConfirmFailedException extends BusinessException {

    public PaymentConfirmFailedException(PaymentConfirmErrorCode errorCode) {
        super(errorCode);
    }
}
