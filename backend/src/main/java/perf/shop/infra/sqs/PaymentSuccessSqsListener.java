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
        List<Payment> payments = responses.stream().map(Payment::from).toList();
        List<String> orderIds = responses.stream().map(PaymentSuccessMessage::getOrderId).toList();
        paymentService.bulkSavePayments(payments);
        orderService.bulkApproveOrders(orderIds);
        outboxService.bulkUpdateStatusToDone(orderIds);
        log.info("결제 성공 메세지 처리 완료 : {}", responses.size());
    }
}
