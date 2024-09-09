package perf.shop.mock.fixtures.order;

import java.util.List;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.domain.order.dto.request.OrdererRequest;
import perf.shop.domain.payment.dto.request.PaymentApproveRequest;

public class OrderFixture {

    public static OrderCreateRequest createOrderCreateRequest(OrdererRequest orderer, ShippingInfoRequest shippingInfo,
                                                              List<OrderLineRequest> orderLines) {
        return OrderCreateRequest.builder()
                .orderer(orderer)
                .shippingInfo(shippingInfo)
                .orderLines(orderLines)
                .build();
    }

    public static OrderRequest createOrderRequest(OrdererRequest orderer, ShippingInfoRequest shippingInfo,
                                                  List<OrderLineRequest> orderLines,
                                                  PaymentApproveRequest paymentApproveRequest) {
        return OrderRequest.builder()
                .orderer(orderer)
                .shippingInfo(shippingInfo)
                .orderLines(orderLines)
                .paymentApproveRequest(paymentApproveRequest)
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

    public static Order createOrder() {
        return Order.builder()
                .build();
    }

}
