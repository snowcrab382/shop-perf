package perf.shop.infra.sqs;

import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.payment.domain.PaymentStatus;
import perf.shop.domain.payment.domain.PaymentType;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentSuccessMessage {

    private String paymentKey;
    private String orderId;
    private String orderName;
    private Long totalAmount;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;
    private PaymentType type;
    private PaymentStatus status;

    @Builder
    private PaymentSuccessMessage(String paymentKey, String orderId, String orderName, Long totalAmount,
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

    public static PaymentSuccessMessage from(PaymentConfirmResponse response) {
        return PaymentSuccessMessage.builder()
                .paymentKey(response.getPaymentKey())
                .orderId(response.getOrderId())
                .orderName(response.getOrderName())
                .totalAmount(response.getTotalAmount())
                .requestedAt(response.getRequestedAt())
                .approvedAt(response.getApprovedAt())
                .type(response.getType())
                .status(response.getStatus())
                .build();
    }
}
