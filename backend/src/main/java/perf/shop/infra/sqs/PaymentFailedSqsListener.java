package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.application.OrderService;
import perf.shop.domain.outbox.application.OutboxService;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class PaymentFailedSqsListener {

    private final OrderService orderService;
    private final OutboxService outboxService;

    @SqsListener(
            value = "${spring.cloud.aws.sqs.payment-failed}",
            factory = "defaultSqsListenerContainerFactory"
    )
    public void messageListener(PaymentFailedMessage response) {
        orderService.cancelOrder(response.getOrderId());
        outboxService.updateStatusToDone(response.getOrderId());
    }
}
