package perf.shop.domain.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.domain.PaymentInfo;
import perf.shop.domain.payment.repository.PaymentRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void createPayment(Order order, PaymentInfo paymentInfo) {
        Payment newPayment = Payment.of(order, paymentInfo);
        paymentRepository.save(newPayment);
    }

    
}
