package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.List;
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
            value = "${spring.cloud.aws.sqs.payment-success-queue}",
            factory = "defaultSqsListenerContainerFactory"
    )
    public void messageListener(List<PaymentSuccessMessage> response) {
        paymentService.saveAll(response.stream()
                .map(Payment::from)
                .toList());
    }
}
