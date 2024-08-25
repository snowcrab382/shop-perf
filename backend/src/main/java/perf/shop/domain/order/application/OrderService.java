package perf.shop.domain.order.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.domain.OrderLine;
import perf.shop.domain.order.domain.Orderer;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.domain.order.repository.OrdersRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderLineFactory orderLineFactory;

    public Order createOrder(Long userId, OrderRequest request) {
        String orderId = request.getPaymentInfo().getOrderId();
        Orderer orderer = Orderer.from(userId, request.getOrderer());
        ShippingInfo shippingInfo = ShippingInfo.from(request.getShippingInfo());
        List<OrderLine> orderLines = request.getOrderLines().stream()
                .map(orderLineFactory::createOrderLine)
                .toList();
        Order newOrder = Order.of(orderId, orderer, shippingInfo, orderLines);
        ordersRepository.save(newOrder);
        return newOrder;
    }

}
