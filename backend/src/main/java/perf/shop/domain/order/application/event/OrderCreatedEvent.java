package perf.shop.domain.order.application.event;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.payment.dto.request.PaymentRequest;

@Getter
public class OrderCreatedEvent {

    private final PaymentRequest paymentInfo;

    @Builder
    private OrderCreatedEvent(PaymentRequest paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public static OrderCreatedEvent from(PaymentRequest paymentInfo) {
        return OrderCreatedEvent.builder()
                .paymentInfo(paymentInfo)
                .build();
    }

}
