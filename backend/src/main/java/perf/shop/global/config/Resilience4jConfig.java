package perf.shop.global.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.domain.payment.error.exception.PaymentConfirmTemporaryFailedException;

@Configuration
@RequiredArgsConstructor
public class Resilience4jConfig {

    public static final String PAYMENT_CONFIRM_RETRY = "paymentConfirmRetry";
    public static final String PAYMENT_CONFIRM_CIRCUIT_BREAKER = "paymentConfirmCircuitBreaker";

    private static final int MAX_ATTEMPTS = 3;
    private static final int WAIT_DURATION = 500;
    private static final int SLOW_CALL_DURATION_THRESHOLD = 10;
    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Bean
    public Retry paymentConfirmRetry() {
        return retryRegistry.retry(PAYMENT_CONFIRM_RETRY, RetryConfig.custom()
                .maxAttempts(MAX_ATTEMPTS)
                .waitDuration(Duration.ofMillis(WAIT_DURATION))
                .retryExceptions(IOException.class, PaymentConfirmTemporaryFailedException.class,
                        PaymentConfirmFailedException.class)
                .build());
    }

    @Bean
    public CircuitBreaker paymentConfirmCircuitBreaker() {
        return circuitBreakerRegistry.circuitBreaker(PAYMENT_CONFIRM_CIRCUIT_BREAKER, CircuitBreakerConfig.custom()
                .slowCallDurationThreshold(Duration.ofSeconds(SLOW_CALL_DURATION_THRESHOLD))
                .recordExceptions(IOException.class, PaymentConfirmTemporaryFailedException.class)
                .enableAutomaticTransitionFromOpenToHalfOpen()
                .build());
    }
}
