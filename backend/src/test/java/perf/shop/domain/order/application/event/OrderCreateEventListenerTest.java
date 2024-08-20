package perf.shop.domain.order.application.event;

import static org.mockito.BDDMockito.then;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrder;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrderCreateEvent;
import static perf.shop.mock.fixtures.payment.PaymentFixture.createPaymentInfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.payment.application.PaymentService;
import perf.shop.domain.payment.domain.PaymentInfo;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] OrderCreateEventListener")
class OrderCreateEventListenerTest {

    @InjectMocks
    OrderCreateEventListener orderCreateEventListener;

    @Mock
    PaymentService paymentService;

    @Nested
    @DisplayName("결제 생성 이벤트")
    class CreatePayment {

        @Test
        @DisplayName("성공")
        void createPayment() {
            // given
            Order order = createOrder();
            PaymentInfo paymentInfo = createPaymentInfo("CARD", "TOSS");
            OrderCreateEvent event = createOrderCreateEvent(order, paymentInfo);

            // when
            orderCreateEventListener.createPayment(event);

            // then
            then(paymentService).should().createPayment(order, paymentInfo);
        }
    }

}
