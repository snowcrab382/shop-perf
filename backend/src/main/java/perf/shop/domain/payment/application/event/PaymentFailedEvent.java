package perf.shop.domain.payment.application.event;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.payment.domain.Payment;

@Getter
public class PaymentFailedEvent {

    private final Payment payment;

    @Builder
    private PaymentFailedEvent(Payment payment) {
        this.payment = payment;
    }

    public static PaymentFailedEvent from(Payment payment) {
        return PaymentFailedEvent.builder()
                .payment(payment)
                .build();
    }
}
