package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import perf.shop.domain.order.application.OrderService;

@Component
@RequiredArgsConstructor
public class PaymentFailedSqsListener {

    private final OrderService orderService;

    @SqsListener(
            value = "${spring.cloud.aws.sqs.payment-failed-queue}",
            factory = "defaultSqsListenerContainerFactory"
    )
    public void messageListener(List<PaymentFailedMessage> response) {
        response.forEach(message -> {
            orderService.cancelOrder(message.getOrderId());
        });
    }
}
