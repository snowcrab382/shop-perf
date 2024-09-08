package perf.shop.domain.payment.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.domain.payment.error.exception.PaymentStatusUncertainException;
import perf.shop.global.error.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentConfirmFailedException.class)
    protected ErrorResponse handlePaymentConfirmFailedException(PaymentConfirmFailedException e) {
        return ErrorResponse.of(e.getErrorCode());
    }

    @ExceptionHandler(PaymentStatusUncertainException.class)
    protected ErrorResponse handlePaymentStatusUncertainException(PaymentStatusUncertainException e) {
        return ErrorResponse.of(e.getErrorCode());
    }

}
