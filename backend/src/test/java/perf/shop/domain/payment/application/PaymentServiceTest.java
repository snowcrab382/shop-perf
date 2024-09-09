package perf.shop.domain.payment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.payment.dao.PaymentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] PaymentService")
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;


}
