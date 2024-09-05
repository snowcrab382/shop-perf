package perf.shop.infra.sqs;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessMessageSender {

    private final SqsTemplate sqsTemplate;
    @Value("${spring.cloud.aws.sqs.payment-success}")
    private String paymentSuccessQueue;

    public void sendMessage(PaymentSuccessMessage message) {
        sqsTemplate.send(to -> to.queue(paymentSuccessQueue).payload(message));
    }
}
