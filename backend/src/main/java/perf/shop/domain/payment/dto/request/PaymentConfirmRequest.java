package perf.shop.domain.payment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentConfirmRequest {

    private final String paymentKey;
    private final String orderNumber;
    private final Long amount;

    @Builder
    private PaymentConfirmRequest(String paymentKey, String orderNumber, Long amount) {
        this.paymentKey = paymentKey;
        this.orderNumber = orderNumber;
        this.amount = amount;
    }
}
