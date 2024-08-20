package perf.shop.domain.order.application.event;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.payment.domain.PaymentInfo;

@Getter
public class OrderCreateEvent {

    private final Order order;
    private final PaymentInfo paymentInfo;

    @Builder
    private OrderCreateEvent(Order order, PaymentInfo paymentInfo) {
        this.order = order;
        this.paymentInfo = paymentInfo;
    }

    public static OrderCreateEvent of(Order order, PaymentInfo paymentInfo) {
        return OrderCreateEvent.builder()
                .order(order)
                .paymentInfo(paymentInfo)
                .build();
    }
}
