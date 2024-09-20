package perf.shop.infra.sqs;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SQSConfig {

//    @Value("${spring.cloud.aws.region.static}")
//    private String REGION;
//
//    @Value("${spring.cloud.aws.credentials.access-key}")
//    private String ACCESS_KEY;
//
//    @Value("${spring.cloud.aws.credentials.secret-key}")
//    private String SECRET_KEY;
//
//    @Bean
//    public SqsAsyncClient sqsAsyncClient() {
//        return SqsAsyncClient.builder()
//                .credentialsProvider(() -> new AwsCredentials() {
//                    @Override
//                    public String accessKeyId() {
//                        return ACCESS_KEY;
//                    }
//
//                    @Override
//                    public String secretAccessKey() {
//                        return SECRET_KEY;
//                    }
//                })
//                .region(Region.of(REGION))
//                .build();
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return objectMapper;
//    }
//
//    @Bean
//    public SqsMessagingMessageConverter messagingMessageConverter() {
//        SqsMessagingMessageConverter converter = new SqsMessagingMessageConverter();
//        converter.setObjectMapper(objectMapper());
//        return converter;
//    }

//    @Bean
//    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
//        return SqsMessageListenerContainerFactory.builder()
//                .configure(options -> options
//                        .messageConverter(messagingMessageConverter())
//                        .maxConcurrentMessages(100)
//                        .maxMessagesPerPoll(100)
//                        .pollTimeout(Duration.ofSeconds(20))
//                )
//                .sqsAsyncClient(sqsAsyncClient())
//                .build();
//    }

//    @Bean
//    public SqsTemplate sqsTemplate() {
//        return SqsTemplate.builder()
//                .sqsAsyncClient(sqsAsyncClient())
//                .messageConverter(messagingMessageConverter())
//                .build();
//    }

}
