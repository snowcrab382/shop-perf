package perf.shop.domain.payment.application.event;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.payment.domain.Payment;

@Getter
public class PaymentCompletedEvent {

    private final Payment payment;

    @Builder
    private PaymentCompletedEvent(Payment payment) {
        this.payment = payment;
    }

    public static PaymentCompletedEvent from(Payment payment) {
        return PaymentCompletedEvent.builder()
                .payment(payment)
                .build();
    }
}
