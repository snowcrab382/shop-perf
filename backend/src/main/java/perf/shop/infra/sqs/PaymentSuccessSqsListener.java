package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.outbox.application.OutboxService;
import perf.shop.domain.payment.application.PaymentService;
import perf.shop.domain.payment.domain.Payment;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class PaymentSuccessSqsListener {

    private final PaymentService paymentService;
    private final OutboxService outboxService;

    @SqsListener(
            value = "${spring.cloud.aws.sqs.payment-success}",
            factory = "defaultSqsListenerContainerFactory"
    )
    public void messageListener(PaymentSuccessMessage response) {
        paymentService.savePayment(Payment.from(response));
        outboxService.updateStatusToDone(response.getOrderId());
    }
}
