package perf.shop.domain.order.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import perf.shop.domain.payment.application.PaymentService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventListener {

    private final PaymentService paymentService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        paymentService.approvePayment(event.getPaymentInfo());
    }

}
