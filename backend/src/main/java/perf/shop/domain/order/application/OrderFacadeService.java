package perf.shop.domain.order.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.domain.payment.application.PaymentClient;
import perf.shop.domain.payment.application.event.PaymentEventPublisher;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderFacadeService {

    private final OrderService orderService;
    private final PaymentClient paymentClient;
    private final PaymentEventPublisher paymentEventPublisher;

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
        //트랜잭션 1
        orderService.createOrder(userId, request);

        //트랜잭션 X, 비동기 처리
        try {
            PaymentConfirmResponse response = paymentClient.fakeConfirmPayment(request.getPaymentInfo());
            Payment payment = Payment.from(response);
            paymentEventPublisher.publishPaymentApprovedEvent(payment); // 이벤트 처리
        } catch (Exception e) {
            log.error("{}", e.getClass());
//            paymentEventPublisher.publishPaymentFailedEvent(request.get);
        }
    }
}
