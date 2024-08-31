package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import perf.shop.domain.order.application.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedSqsListener {

    private final OrderService orderService;

    @SqsListener(
            value = "${spring.cloud.aws.sqs.payment-failed}",
            factory = "defaultSqsListenerContainerFactory"
    )
    public void messageListener(PaymentFailedMessage response) {
        orderService.cancelOrder(response.getOrderId());
    }
}
