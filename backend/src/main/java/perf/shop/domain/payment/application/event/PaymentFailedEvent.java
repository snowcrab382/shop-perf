package perf.shop.domain.payment.application.event;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.order.domain.Order;

@Getter
public class PaymentFailedEvent {

    private final Order order;

    @Builder
    private PaymentFailedEvent(Order order) {
        this.order = order;
    }

    public static PaymentFailedEvent from(Order order) {
        return PaymentFailedEvent.builder()
                .order(order)
                .build();
    }
}
