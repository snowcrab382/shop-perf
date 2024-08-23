package perf.shop.domain.payment.application;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import perf.shop.domain.payment.error.exception.PaymentConfirmErrorCode;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.domain.payment.error.exception.PaymentConfirmTimeoutException;

public class PaymentExceptionInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        try {
            return execution.execute(request, body);
        } catch (IOException e) {
            throw new PaymentConfirmTimeoutException(PaymentConfirmErrorCode.PAYMENT_REQUEST_TIMEOUT);
        } catch (Exception e) {
            throw new PaymentConfirmFailedException(PaymentConfirmErrorCode.UNEXPECTED_PAYMENT_ERROR);
        }
    }
}
