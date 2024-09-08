package perf.shop.domain.payment.error.exception;

import perf.shop.global.error.exception.BusinessException;
import perf.shop.global.error.exception.ErrorCode;

public class PaymentStatusUncertainException extends BusinessException {

    public PaymentStatusUncertainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
