package perf.shop.domain.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.dto.request.PaymentRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.domain.payment.repository.PaymentRepository;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;

    public Payment approvePayment(PaymentRequest request) {
//        PaymentConfirmResponse response = paymentClient.confirmPayment(request);
        PaymentConfirmResponse fakeResponse = paymentClient.fakeConfirmPayment(request);
        return paymentRepository.save(Payment.from(fakeResponse));
    }
}
