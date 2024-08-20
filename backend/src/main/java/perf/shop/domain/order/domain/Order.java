package perf.shop.domain.order.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Orderer orderer;

    @Embedded
    private ShippingInfo shippingInfo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Builder
    private Order(Orderer orderer, ShippingInfo shippingInfo) {
        this.orderer = orderer;
        this.shippingInfo = shippingInfo;
        this.state = OrderState.CREATED;
    }

    public static Order of(Orderer orderer, ShippingInfo shippingInfo, List<OrderLine> orderLines) {
        Order newOrder = Order.builder()
                .orderer(orderer)
                .shippingInfo(shippingInfo)
                .build();

        orderLines.forEach(newOrder::addOrderLine);
        return newOrder;
    }

    public void addOrderLine(OrderLine orderLine) {
    private void addOrderLine(OrderLine orderLine) {
        orderLine.setOrder(this);
        orderLines.add(orderLine);
    }

    public Long calculateTotalAmounts() {
        return orderLines.stream()
                .mapToLong(OrderLine::getAmounts)
                .sum();
    }


}
