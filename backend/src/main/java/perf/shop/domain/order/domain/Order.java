package perf.shop.domain.order.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.global.common.domain.BaseEntity;
import perf.shop.global.error.exception.GlobalErrorCode;
import perf.shop.global.error.exception.InvalidValueException;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    private String id;

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
        this.id = generateOrderId();
        this.orderer = orderer;
        this.shippingInfo = shippingInfo;
        this.state = OrderState.CREATED;
    }

    public static Order of(Orderer orderer, ShippingInfo shippingInfo, List<OrderLine> orderLines) {
        Order newOrder = Order.builder()
                .orderer(orderer)
                .shippingInfo(shippingInfo)
                .build();

        verifyOrderLines(orderLines);
        orderLines.forEach(newOrder::addOrderLine);
        return newOrder;
    }

    private static void verifyOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new InvalidValueException(GlobalErrorCode.ORDER_LINE_NOT_EXIST);
        }
    }

    public Long calculateTotalAmounts() {
        return orderLines.stream()
                .mapToLong(OrderLine::getAmounts)
                .sum();
    }

    private void addOrderLine(OrderLine orderLine) {
        orderLine.setOrder(this);
        orderLines.add(orderLine);
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
