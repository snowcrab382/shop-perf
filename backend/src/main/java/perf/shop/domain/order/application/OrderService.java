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
import perf.shop.domain.product.application.ProductService;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final OrdersRepository ordersRepository;
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
        productService.decreaseStocksWithImplicitLock(newOrder);
        return newOrder;
    }

    public void cancelOrder(String id) {
        Order order = getOrder(id);
        productService.increaseStocksWithImplicitLock(order);
        order.failed();
        ordersRepository.save(order);
    }

    private Order getOrder(String id) {
        return ordersRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(GlobalErrorCode.ORDER_NOT_FOUND)
        );
    }

}
