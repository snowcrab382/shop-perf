package perf.shop.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import perf.shop.domain.order.domain.OrderLine;
import perf.shop.domain.order.dto.request.OrderLineRequest;
import perf.shop.domain.product.application.ProductService;

@Component
@RequiredArgsConstructor
public class OrderLineFactory {

    private final ProductService productService;

    public OrderLine createOrderLine(OrderLineRequest request) {
        productService.validateProductExistsById(request.getProductId());
        return OrderLine.from(request);
    }
}
