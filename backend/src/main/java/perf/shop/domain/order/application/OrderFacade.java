package perf.shop.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.domain.payment.application.TossPaymentService;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderProcessService orderProcessService;
    private final TossPaymentService paymentService;

    public void processOrder(Long userId, OrderRequest orderRequest) {
        orderProcessService.processOrderCreation(userId, orderRequest);
        paymentService.processPaymentApprove(orderRequest.getPaymentApproveRequest());
    }
}
