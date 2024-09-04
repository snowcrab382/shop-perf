package perf.shop.domain.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.application.OrderService;
import perf.shop.domain.outbox.application.OutboxService;
import perf.shop.domain.payment.domain.Payment;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentFacadeService {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final OutboxService outboxService;

    public void pay(Payment payment) {
        paymentService.savePayment(payment);
        orderService.approveOrder(payment.getOrderId());
//        outboxService.updateStatusToDone(payment.getOrderId());
    }
}
