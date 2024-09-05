package perf.shop.domain.payment.application.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentFailedEvent {

    private final String orderId;

    @Builder
    private PaymentFailedEvent(String orderId) {
        this.orderId = orderId;
    }

    public static PaymentFailedEvent from(String orderId) {
        return PaymentFailedEvent.builder()
                .orderId(orderId)
                .build();
    }
}
