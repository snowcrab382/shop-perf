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

    public void processOrderAsync(Long userId, OrderRequest orderRequest) {
        orderProcessService.processOrderCreation(userId, orderRequest);
        paymentService.processPaymentApproveAsync(orderRequest.getPaymentApproveRequest());
    }

    public void processOrderSync(Long userId, OrderRequest orderRequest) {
        orderProcessService.processOrderCreation(userId, orderRequest);
        paymentService.processPaymentApproveSync(orderRequest.getPaymentApproveRequest());
    }

    public void processOrderWithPaymentApprove(OrderRequest orderRequest) {
        paymentService.processPaymentApproveOnly(orderRequest.getPaymentApproveRequest());
    }
}
