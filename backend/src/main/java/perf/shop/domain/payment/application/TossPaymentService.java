package perf.shop.domain.payment.application;


import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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
    private final PaymentApprovedService paymentApprovedService;

    public void processPaymentApproveAsync(PaymentApproveRequest request) {
        try {
            Payment newPayment = confirmPayment(request);
//            paymentEventPublisher.publishPaymentApprovedEvent(newPayment);
        } catch (PaymentConfirmFailedException | CallNotPermittedException e) {
//            paymentEventPublisher.publishPaymentFailedEvent(request.getOrderId());
//            throw e;
        }
    }

    public void processPaymentApproveSync(PaymentApproveRequest request) {
        Payment newPayment = confirmPayment(request);
        paymentApprovedService.processPaymentApproved(newPayment);
    }

    public void processPaymentApproveOnly(PaymentApproveRequest request) {
        Payment newPayment = confirmPayment(request);
    }

    private Payment confirmPayment(PaymentApproveRequest request) {
        PaymentConfirmResponse paymentConfirmResponse = paymentClient.fakeConfirmPayment(request);
        return Payment.from(paymentConfirmResponse);
    }
}
