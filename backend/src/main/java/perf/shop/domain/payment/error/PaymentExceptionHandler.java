package perf.shop.domain.payment.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.domain.payment.error.exception.PaymentConfirmTimeoutException;
import perf.shop.global.error.ErrorResponse;

@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentConfirmFailedException.class)
    protected ErrorResponse handlePaymentConfirmFailedException(PaymentConfirmFailedException e) {
        return ErrorResponse.of(e.getErrorCode());
    }

    @ExceptionHandler(PaymentConfirmTimeoutException.class)
    protected ErrorResponse handlePaymentConfirmTimeoutException(PaymentConfirmTimeoutException e) {
        return ErrorResponse.of(e.getErrorCode());
    }

}
