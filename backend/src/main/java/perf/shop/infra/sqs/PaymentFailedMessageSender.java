package perf.shop.infra.sqs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFailedMessageSender {

//    private final SqsTemplate sqsTemplate;
//
//    @Value("${spring.cloud.aws.sqs.payment-failed}")
//    private String paymentFailedQueue;
//
//    public void sendMessage(PaymentFailedMessage message) {
//        sqsTemplate.send(to -> to.queue(paymentFailedQueue).payload(message));
//    }
}
