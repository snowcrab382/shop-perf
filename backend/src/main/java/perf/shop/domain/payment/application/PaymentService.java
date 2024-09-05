package perf.shop.domain.payment.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.payment.dao.BatchPaymentRepository;
import perf.shop.domain.payment.dao.PaymentRepository;
import perf.shop.domain.payment.domain.Payment;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BatchPaymentRepository batchPaymentRepository;

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void bulkSavePayments(List<Payment> payments) {
        batchPaymentRepository.bulkInsert(payments);
    }

}
