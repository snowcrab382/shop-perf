package perf.shop.domain.payment.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import perf.shop.domain.payment.domain.Payment;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishPaymentApprovedEvent(Payment payment) {
        PaymentApprovedEvent event = PaymentApprovedEvent.from(payment);
        eventPublisher.publishEvent(event);
    }

    public void publishPaymentFailedEvent(Payment payment) {
        PaymentFailedEvent event = PaymentFailedEvent.from(payment);
        eventPublisher.publishEvent(event);
    }
}
