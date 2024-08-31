package perf.shop.infra.sqs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.ListenerMode;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SQSConfig {

    @Value("${spring.cloud.aws.region.static}")
    private String REGION;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String ACCESS_KEY;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String SECRET_KEY;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return ACCESS_KEY;
                    }

                    @Override
                    public String secretAccessKey() {
                        return SECRET_KEY;
                    }
                })
                .region(Region.of(REGION))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public SqsMessagingMessageConverter messagingMessageConverter() {
        SqsMessagingMessageConverter converter = new SqsMessagingMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
                .configure(options -> options
                        .messageConverter(messagingMessageConverter())
                        .listenerMode(ListenerMode.SINGLE_MESSAGE) // 배치 모드
                        .acknowledgementMode(AcknowledgementMode.ON_SUCCESS)
                )
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate() {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .configureDefaultConverter(converter -> {
                    converter.setObjectMapper(objectMapper());
                })
                .build();
    }

}
