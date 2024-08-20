package perf.shop.domain.order.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.order.application.event.OrderCreateEvent;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.domain.OrderLine;
import perf.shop.domain.order.domain.Orderer;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.domain.order.repository.OrdersRepository;
import perf.shop.domain.payment.domain.PaymentInfo;

@Transactional
@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderLineFactory orderLineFactory;
    private final ApplicationEventPublisher eventPublisher;

    public void createOrder(Long userId, OrderCreateRequest request) {
        Orderer orderer = Orderer.from(userId, request.getOrderer());
        ShippingInfo shippingInfo = ShippingInfo.from(request.getShippingInfo());
        List<OrderLine> orderLines = request.getOrderLines().stream()
                .map(orderLineFactory::createOrderLine)
                .toList();

        Order newOrder = Order.of(orderer, shippingInfo, orderLines);
        ordersRepository.save(newOrder);

        PaymentInfo paymentInfo = PaymentInfo.from(request.getPaymentInfo());
        eventPublisher.publishEvent(OrderCreateEvent.of(newOrder, paymentInfo));
    }
}
