package perf.shop.domain.payment.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.application.OrderService;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.outbox.application.OutboxService;
import perf.shop.domain.payment.application.PaymentService;
import perf.shop.domain.product.application.ProductService;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class PaymentEventHandler {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final OutboxService outboxService;
    private final ProductService productService;

    @Async("taskExecutor")
    @EventListener
    public void handlePaymentCompletedEvent(PaymentApprovedEvent event) {
        paymentService.savePayment(event.getPayment());
        orderService.approveOrder(event.getPayment().getOrderId());
        outboxService.updateStatusToDone(event.getPayment().getOrderId());
    }

    @Async("taskExecutor")
    @EventListener
    public void handlePaymentFailedEvent(PaymentFailedEvent event) {
        Order order = orderService.failedOrder(event.getPayment().getOrderId());
        productService.restoreStock(order);
        outboxService.updateStatusToDone(order.getId());
    }
}
