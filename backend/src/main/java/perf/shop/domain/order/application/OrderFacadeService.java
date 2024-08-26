package perf.shop.domain.order.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.domain.payment.application.PaymentService;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.product.application.ProductService;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class OrderFacadeService {

    private final OrderService orderService;
    private final ProductService productService;
    private final PaymentService paymentService;

    /**
     * 주문 로직
     * <br>
     * 1. 주문 생성
     * <br>
     * 2. 주문 검증(실제 주문 계산 금액과 요청으로 받은 결제 금액 비교)
     * <br>
     * 3. 주문 상품 재고 차감
     * <br>
     * 4. 토스페이 API로 결제 승인 요청
     * <br>
     * 5. 결제 정보 저장
     */
    public void order(Long userId, OrderRequest request) {
        Order newOrder = orderService.createOrder(userId, request);
        newOrder.verifyAmount(request.getPaymentInfo().getAmount());
        productService.deductStocksWithOutLock(newOrder);
        Payment newPayment = paymentService.approvePayment(request.getPaymentInfo());

    }
}
