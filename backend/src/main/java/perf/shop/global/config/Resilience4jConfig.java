package perf.shop.global.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perf.shop.domain.payment.error.exception.PaymentConfirmTemporaryFailedException;

@Configuration
@RequiredArgsConstructor
public class Resilience4jConfig {

    private static final int MAX_ATTEMPTS = 3;
    private static final int WAIT_DURATION = 500;

    private final RetryRegistry retryRegistry;

    @Bean
    public Retry paymentConfirmRetry() {
        return retryRegistry.retry("paymentConfirmRetry", RetryConfig.custom()
                .maxAttempts(MAX_ATTEMPTS)
                .waitDuration(Duration.ofMillis(WAIT_DURATION))
                .retryExceptions(IOException.class, PaymentConfirmTemporaryFailedException.class)
                .build());
    }
}
