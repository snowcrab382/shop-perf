package perf.shop.domain.payment.dto.response;

import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.payment.domain.PaymentStatus;
import perf.shop.domain.payment.domain.PaymentType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentConfirmResponse {

    private String paymentKey;
    private String orderId;
    private String orderName;
    private Long totalAmount;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;
    private PaymentType type;
    private PaymentStatus status;

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
