package perf.shop.domain.payment.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.repository.PaymentRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void saveAll(List<Payment> payments) {
        paymentRepository.saveAll(payments);
    }

}
