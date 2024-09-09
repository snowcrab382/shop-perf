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
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;
import perf.shop.global.error.exception.InvalidValueException;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository ordersRepository;
    private final OrderLineFactory orderLineFactory;

    public Order createOrder(Long userId, OrderRequest request) {
        validateOrder(request.getPaymentApproveRequest().getOrderId());
        Order newOrder = createOrderFromRequest(userId, request);
        newOrder.verifyAmount(request.getPaymentApproveRequest().getAmount());
        ordersRepository.save(newOrder);
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

    private void validateOrder(String orderId) {
        if (ordersRepository.existsById(orderId)) {
            throw new InvalidValueException(GlobalErrorCode.ORDER_ALREADY_EXISTS);
        }
    }

    private Order createOrderFromRequest(Long userId, OrderRequest request) {
        String orderId = getOrderId(request);
        Orderer orderer = createOrderer(userId, request);
        ShippingInfo shippingInfo = createShippingInfo(request);
        List<OrderLine> orderLines = createOrderLines(request);

        return Order.of(orderId, orderer, shippingInfo, orderLines);
    }

    private String getOrderId(OrderRequest request) {
        return request.getPaymentApproveRequest().getOrderId();
    }

    private Orderer createOrderer(Long userId, OrderRequest request) {
        return Orderer.from(userId, request.getOrderer());
    }

    private ShippingInfo createShippingInfo(OrderRequest request) {
        return ShippingInfo.from(request.getShippingInfo());
    }

    private List<OrderLine> createOrderLines(OrderRequest request) {
        return request.getOrderLines().stream()
                .map(orderLineFactory::createOrderLine)
                .toList();
    }
}
