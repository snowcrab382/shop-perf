package perf.shop.domain.payment.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static perf.shop.mock.fixtures.order.OrderFixture.createOrder;
import static perf.shop.mock.fixtures.payment.PaymentFixture.createPayment;
import static perf.shop.mock.fixtures.payment.PaymentFixture.createPaymentInfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.domain.PaymentInfo;
import perf.shop.domain.payment.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] PaymentService")
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Nested
    @DisplayName("결제 생성 테스트")
    class CreatePayment {

        @Test
        @DisplayName("성공")
        void createPayment_success() {
            // given
            Order order = createOrder();
            PaymentInfo paymentInfo = createPaymentInfo("CARD", "TOSS");
            Payment newPayment = createPayment(order, paymentInfo);
            given(paymentRepository.save(any(Payment.class))).willReturn(newPayment);

            // when
            paymentService.createPayment(order, paymentInfo);

            // then
            then(paymentRepository).should().save(any(Payment.class));
        }
    }

}
