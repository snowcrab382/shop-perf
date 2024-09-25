package perf.shop.domain.payment.application;


import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import perf.shop.domain.payment.dto.request.PaymentApproveRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.infra.sqs.application.MessageSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final PaymentClient paymentClient;
    private final MessageSender messageSender;
    
    @Value("${spring.cloud.aws.sqs.payment-failed-queue}")
    private String PAYMENT_FAILED_QUEUE;
    @Value("${spring.cloud.aws.sqs.payment-success-queue}")
    private String PAYMENT_SUCCESS_QUEUE;

    public void processPayment(PaymentApproveRequest request) {
        try {
            PaymentConfirmResponse response = paymentClient.fakeConfirmPayment(request);
            messageSender.send(PAYMENT_SUCCESS_QUEUE, response);
        } catch (PaymentConfirmFailedException | CallNotPermittedException e) {
            messageSender.send(PAYMENT_FAILED_QUEUE, request.getOrderId());
            throw e;
        }
    }

}
