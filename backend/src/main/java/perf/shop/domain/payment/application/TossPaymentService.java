package perf.shop.domain.payment.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import perf.shop.domain.payment.application.event.PaymentEventPublisher;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.dto.request.PaymentApproveRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final PaymentEventPublisher paymentEventPublisher;
    private final PaymentClient paymentClient;

    public void processPaymentApprove(PaymentApproveRequest request) {
        try {
            Payment newPayment = confirmPayment(request);
            paymentEventPublisher.publishPaymentApprovedEvent(newPayment);
        } catch (PaymentConfirmFailedException e) {
            paymentEventPublisher.publishPaymentFailedEvent(request.getOrderId());
            throw e;
        }
    }

    private Payment confirmPayment(PaymentApproveRequest request) {
        PaymentConfirmResponse paymentConfirmResponse = paymentClient.confirmPayment(request);
        return Payment.from(paymentConfirmResponse);
    }
}
