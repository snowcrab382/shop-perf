package perf.shop.domain.outbox.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "outbox", indexes = @Index(name = "idx_order_id", columnList = "order_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Builder
    private Outbox(String orderId) {
        this.orderId = orderId;
        this.status = OutboxStatus.READY;
    }

    public static Outbox from(String orderId) {
        return Outbox.builder()
                .orderId(orderId)
                .build();
    }

    public void Done() {
        this.status = OutboxStatus.DONE;
    }
}
