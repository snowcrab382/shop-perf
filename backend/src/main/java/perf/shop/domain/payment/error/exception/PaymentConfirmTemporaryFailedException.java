package perf.shop.domain.payment.error.exception;

import perf.shop.global.error.exception.BusinessException;

public class PaymentConfirmTemporaryFailedException extends BusinessException {

    public PaymentConfirmTemporaryFailedException(PaymentConfirmErrorCode errorCode) {
        super(errorCode);
    }
}
