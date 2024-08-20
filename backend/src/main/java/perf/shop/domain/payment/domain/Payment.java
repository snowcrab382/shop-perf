package perf.shop.domain.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.order.domain.Order;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long amount;

    @Embedded
    private PaymentInfo paymentInfo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Builder
    private Payment(Order order, PaymentInfo paymentInfo) {
        this.order = order;
        this.amount = order.calculateTotalAmounts();
        this.paymentInfo = paymentInfo;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment of(Order order, PaymentInfo paymentInfo) {
        return Payment.builder()
                .order(order)
                .paymentInfo(paymentInfo)
                .build();
    }

}
