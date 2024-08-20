package perf.shop.mock.fixtures.order;

import java.util.List;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.domain.order.application.event.OrderCreateEvent;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.order.dto.request.OrdererRequest;
import perf.shop.domain.order.dto.request.PaymentInfoRequest;
import perf.shop.domain.payment.domain.PaymentInfo;

public class OrderFixture {

    public static OrderCreateRequest createOrderCreateRequest(OrdererRequest orderer, ShippingInfoRequest shippingInfo,
                                                              List<OrderLineRequest> orderLines,
                                                              PaymentInfoRequest paymentInfo) {
        return OrderCreateRequest.builder()
                .orderer(orderer)
                .shippingInfo(shippingInfo)
                .orderLines(orderLines)
                .paymentInfo(paymentInfo)
                .build();
    }

    public static OrdererRequest createOrdererRequest(String ordererName, String ordererEmail) {
        return OrdererRequest.builder()
                .ordererName(ordererName)
                .ordererEmail(ordererEmail)
                .build();
    }

    public static OrderLineRequest createOrderLineRequest(Long productId, Integer quantity, Long price) {
        return OrderLineRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .build();
    }

    public static PaymentInfoRequest createPaymentInfoRequest(String method, String type) {
        return PaymentInfoRequest.builder()
                .method(method)
                .type(type)
                .build();
    }

    public static Order createOrder() {
        return Order.builder()
                .build();
    }

    public static OrderCreateEvent createOrderCreateEvent(Order order, PaymentInfo paymentInfo) {
        return OrderCreateEvent.builder()
                .order(order)
                .paymentInfo(paymentInfo)
                .build();
    }
}
