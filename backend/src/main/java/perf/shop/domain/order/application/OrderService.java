package perf.shop.domain.order.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.domain.order.dao.OrderRepository;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.domain.OrderLine;
import perf.shop.domain.order.domain.Orderer;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.domain.outbox.application.OutboxService;
import perf.shop.domain.product.application.ProductService;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final OutboxService outboxService;
    private final OrderRepository ordersRepository;
    private final OrderLineFactory orderLineFactory;

    public Order createOrder(Long userId, OrderRequest request) {
        String orderId = request.getPaymentInfo().getOrderId();
        Orderer orderer = Orderer.from(userId, request.getOrderer());
        ShippingInfo shippingInfo = ShippingInfo.from(request.getShippingInfo());
        List<OrderLine> orderLines = request.getOrderLines().stream()
                .map(orderLineFactory::createOrderLine)
                .toList();
        Order newOrder = ordersRepository.save(Order.of(orderId, orderer, shippingInfo, orderLines));

        newOrder.verifyAmount(request.getPaymentInfo().getAmount());
        productService.deductStock(newOrder);
        outboxService.createOutbox(newOrder.getId());
        return newOrder;
    }

    public void approveOrder(String id) {
        Order order = getOrder(id);
        order.paymentApproved();
        ordersRepository.save(order);
    }

    public Order failedOrder(String id) {
        Order order = getOrder(id);
        order.failed();
        return ordersRepository.save(order);
    }

    private Order getOrder(String id) {
        return ordersRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(GlobalErrorCode.ORDER_NOT_FOUND)
        );
    }

}
