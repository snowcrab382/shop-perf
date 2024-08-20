package perf.shop.domain.order.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import perf.shop.domain.payment.application.PaymentService;

@Component
@RequiredArgsConstructor
public class OrderCreateEventListener {

    private final PaymentService paymentService;

    //TODO : 이벤트 처리 실패 시 재시도 로직 구현 필요
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createPayment(OrderCreateEvent event) {
        paymentService.createPayment(event.getOrder(), event.getPaymentInfo());
    }

}
