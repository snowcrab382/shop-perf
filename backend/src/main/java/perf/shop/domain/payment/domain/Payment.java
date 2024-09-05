package perf.shop.domain.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.global.common.domain.BaseEntity;
import perf.shop.infra.sqs.PaymentSuccessMessage;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private Long totalAmount;

    @Column(nullable = false)
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private ZonedDateTime requestedAt;

    @Column(nullable = false)
    private ZonedDateTime approvedAt;

    @Builder
    private Payment(String orderId, String orderName, Long totalAmount, String paymentKey, PaymentType type,
                    PaymentStatus status, ZonedDateTime requestedAt, ZonedDateTime approvedAt) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.totalAmount = totalAmount;
        this.paymentKey = paymentKey;
        this.type = type;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
    }

    public static Payment from(PaymentConfirmResponse response) {
        return Payment.builder()
                .orderId(response.getOrderId())
                .orderName(response.getOrderName())
                .totalAmount(response.getTotalAmount())
                .paymentKey(response.getPaymentKey())
                .type(response.getType())
                .status(response.getStatus())
                .requestedAt(response.getRequestedAt())
                .approvedAt(response.getApprovedAt())
                .build();
    }

    public static Payment from(PaymentSuccessMessage message) {
        return Payment.builder()
                .orderId(message.getOrderId())
                .orderName(message.getOrderName())
                .totalAmount(message.getTotalAmount())
                .paymentKey(message.getPaymentKey())
                .type(message.getType())
                .status(message.getStatus())
                .requestedAt(message.getRequestedAt())
                .approvedAt(message.getApprovedAt())
                .build();
    }

}
