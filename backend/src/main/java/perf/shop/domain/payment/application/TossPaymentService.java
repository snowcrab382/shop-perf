package perf.shop.domain.payment.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import perf.shop.domain.payment.application.event.PaymentEventPublisher;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.dto.request.PaymentApproveRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final PaymentEventPublisher paymentEventPublisher;
    private final PaymentClient paymentClient;

    public void requestApprove(PaymentApproveRequest request) {
        try {
            Payment newPayment = confirmPayment(request);
            paymentEventPublisher.publishPaymentApprovedEvent(newPayment);
        } catch (Exception e) {
            log.error("결제 승인 요청 중 오류 발생 : {}", e.getClass());
            paymentEventPublisher.publishPaymentFailedEvent(request.getOrderId());
        }

    }

    private Payment confirmPayment(PaymentApproveRequest request) {
        PaymentConfirmResponse paymentConfirmResponse = paymentClient.fakeConfirmPayment(request);
        return Payment.from(paymentConfirmResponse);
    }
}
