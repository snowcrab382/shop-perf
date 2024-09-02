package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.application.OrderService;
import perf.shop.domain.outbox.application.OutboxService;
import perf.shop.domain.payment.application.PaymentService;
import perf.shop.domain.payment.domain.Payment;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class PaymentSuccessSqsListener {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final OutboxService outboxService;

    @SqsListener(
            value = "${spring.cloud.aws.sqs.payment-success}",
            factory = "defaultSqsListenerContainerFactory"
    )
    public void messageListener(List<PaymentSuccessMessage> responses) {
        log.info("결제 성공 메시지 수신: {}", responses.size());
        responses.forEach(response -> {
            Payment payment = Payment.from(response);
            paymentService.savePayment(payment);
            orderService.approveOrder(payment.getOrderId());
            outboxService.updateStatusToDone(payment.getOrderId());
        });
    }
}
