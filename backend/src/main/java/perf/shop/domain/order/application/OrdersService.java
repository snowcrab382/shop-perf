package perf.shop.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.domain.OrderLine;
import perf.shop.domain.order.domain.Orderer;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.repository.OrdersRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public void createOrder(Long userId, OrderCreateRequest request) {
        Orderer orderer = Orderer.from(userId, request.getOrderer());
        ShippingInfo shippingInfo = ShippingInfo.from(request.getShippingInfo());
        Order newOrder = Order.of(orderer, shippingInfo);

        request.getOrderLines().forEach(orderLineRequest -> {
            OrderLine orderLine = OrderLine.of(newOrder, orderLineRequest);
            newOrder.addOrderLine(orderLine);
        });

        ordersRepository.save(newOrder);
    }
}
