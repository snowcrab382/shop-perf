package perf.shop.domain.payment.application.event;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.payment.domain.Payment;

@Getter
public class PaymentApprovedEvent {

    private final Payment payment;

    @Builder
    private PaymentApprovedEvent(Payment payment) {
        this.payment = payment;
    }

    public static PaymentApprovedEvent from(Payment payment) {
        return PaymentApprovedEvent.builder()
                .payment(payment)
                .build();
    }
}
