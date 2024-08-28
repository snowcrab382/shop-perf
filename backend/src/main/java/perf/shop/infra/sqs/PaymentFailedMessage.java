package perf.shop.infra.sqs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentFailedMessage {

    private String orderId;

    @Builder
    private PaymentFailedMessage(String orderId) {
        this.orderId = orderId;
    }

    public static PaymentFailedMessage of(String orderId) {
        return PaymentFailedMessage.builder()
                .orderId(orderId)
                .build();
    }
}
