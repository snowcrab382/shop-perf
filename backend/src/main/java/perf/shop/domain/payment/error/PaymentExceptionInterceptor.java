package perf.shop.domain.payment.error;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import perf.shop.domain.payment.error.exception.PaymentConfirmErrorCode;
import perf.shop.domain.payment.error.exception.PaymentConfirmTemporaryFailedException;
import perf.shop.domain.payment.error.exception.PaymentStatusUncertainException;

@Slf4j
public class PaymentExceptionInterceptor implements ClientHttpRequestInterceptor {

    /**
     * 결제 요청 중 발생하는 일시적 오류 발생 시,
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        try {
            return execution.execute(request, body);
        } catch (IOException | PaymentConfirmTemporaryFailedException e) {
            log.error("결제 요청이 정상적으로 처리되었는지 확인이 필요합니다. : {}", e.getClass());
            throw new PaymentStatusUncertainException(PaymentConfirmErrorCode.PAYMENT_STATUS_UNCERTAIN);
        }
    }
}
