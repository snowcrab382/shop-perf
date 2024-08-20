package perf.shop.domain.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "order_line")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long amounts;

    @Builder
    private OrderLine(Long productId, Integer quantity, Long price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.amounts = calculateAmounts();
    }

    public static OrderLine from(OrderLineRequest request) {
        return OrderLine.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();
    }

    private Long calculateAmounts() {
        return price * quantity;
    }

    void setOrder(Order order) {
        this.order = order;
    }
}
