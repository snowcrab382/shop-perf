package perf.shop.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.domain.outbox.application.OutboxService;
import perf.shop.domain.product.application.ProductService;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderProcessService {

    private final OrderService orderService;
    private final OutboxService outboxService;
    private final ProductService productService;

    public void processOrderCreation(long userId, OrderRequest request) {
        Order newOrder = orderService.createOrder(userId, request);
        outboxService.createOutbox(newOrder.getId());
        productService.deductStockFromOrder(newOrder);
    }
}
