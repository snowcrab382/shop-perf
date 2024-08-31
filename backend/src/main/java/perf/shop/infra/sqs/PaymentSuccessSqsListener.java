package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import perf.shop.domain.payment.application.PaymentService;
import perf.shop.domain.payment.domain.Payment;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessSqsListener {

    private final PaymentService paymentService;

    @SqsListener(
            value = "${spring.cloud.aws.sqs.payment-success}",
            factory = "defaultSqsListenerContainerFactory"
    )
    public void messageListener(PaymentSuccessMessage response) {
        log.info("결제 성공 메세지 수신 : {}", response);
        paymentService.savePayment(Payment.from(response));
        log.info("결제 정보 저장 완료");
    }
}
