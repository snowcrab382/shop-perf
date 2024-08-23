package perf.shop.domain.payment.dto.response;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.payment.domain.PaymentStatus;
import perf.shop.domain.payment.domain.PaymentType;

@Getter
public class PaymentConfirmResponse {

    private final String paymentKey;
    private final String orderId;
    private final String orderName;
    private final Long totalAmount;
    private final ZonedDateTime requestedAt;
    private final ZonedDateTime approvedAt;
    private final PaymentType type;
    private final PaymentStatus status;

    @Builder
    private PaymentConfirmResponse(String paymentKey, String orderId, String orderName, Long totalAmount,
                                   ZonedDateTime requestedAt, ZonedDateTime approvedAt, PaymentType type,
                                   PaymentStatus status) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.totalAmount = totalAmount;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.type = type;
        this.status = status;
    }

}
