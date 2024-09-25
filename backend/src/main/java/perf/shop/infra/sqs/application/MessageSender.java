package perf.shop.infra.sqs.application;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender {

    private final SqsTemplate sqsTemplate;

    public <T> void send(String queueName, T message) {
        sqsTemplate.send(to -> to.queue(queueName).payload(message));
    }
}
