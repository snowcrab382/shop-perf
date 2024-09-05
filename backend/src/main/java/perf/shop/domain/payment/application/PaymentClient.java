package perf.shop.domain.payment.application;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import perf.shop.domain.payment.domain.PaymentStatus;
import perf.shop.domain.payment.domain.PaymentType;
import perf.shop.domain.payment.dto.request.PaymentRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.domain.payment.error.exception.PaymentConfirmErrorCode;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.global.config.properties.PaymentProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentClient {

    private static final String BASIC_DELIMITER = ":";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final int REQUEST_TIMEOUT = 3;

    private final PaymentProperties paymentProperties;
    private final WebClient webClient;


    public PaymentConfirmResponse confirmPayment(PaymentRequest paymentRequest) {
        return webClient
                .post()
                .uri(paymentProperties.getConfirmUrl())
                .header(HttpHeaders.AUTHORIZATION, createPaymentAuthHeader(paymentProperties))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(PaymentConfirmResponse.class)
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                .doOnError(throwable -> {
                    WebClientResponseException exception = (WebClientResponseException) throwable;
                    log.error("주문 번호 {} 에 대한 결제 승인 요청 중 오류 발생 : {}", paymentRequest.getOrderId(),
                            exception.getResponseBodyAsString());
                    throw new PaymentConfirmFailedException(PaymentConfirmErrorCode.findByName(exception.getMessage()));
                })
                .block();

    }

    public PaymentConfirmResponse fakeConfirmPayment(PaymentRequest paymentRequest) {
        PaymentConfirmResponse fakeResponse = PaymentConfirmResponse.builder()
                .paymentKey(paymentRequest.getPaymentKey())
                .orderId(paymentRequest.getOrderId())
                .orderName("testOrderName")
                .totalAmount(paymentRequest.getAmount())
                .requestedAt(ZonedDateTime.now())
                .approvedAt(ZonedDateTime.now())
                .type(PaymentType.CARD)
                .status(PaymentStatus.DONE)
                .build();
        Random random = new Random();
        int delay = 1000;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }

        return fakeResponse;
    }

    public PaymentConfirmResponse checkPaymentByOrderId(String orderId) {
        return webClient
                .get()
                .uri(paymentProperties.getCheckUrl() + orderId)
                .header(HttpHeaders.AUTHORIZATION, createPaymentAuthHeader(paymentProperties))
                .retrieve()
                .bodyToMono(PaymentConfirmResponse.class)
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                .doOnError(throwable -> {
                    WebClientResponseException exception = (WebClientResponseException) throwable;
                    log.error("주문 번호 {} 에 대한 결제 확인 중 오류 발생 : {}", orderId,
                            exception.getResponseBodyAsString());
                    throw new PaymentConfirmFailedException(PaymentConfirmErrorCode.findByName(exception.getMessage()));
                })
                .block();
    }

    private String createPaymentAuthHeader(PaymentProperties paymentProperties) {
        byte[] encodedBytes = Base64.getEncoder().encode((paymentProperties.getSecretKey() + BASIC_DELIMITER).getBytes(
                StandardCharsets.UTF_8));
        return AUTH_HEADER_PREFIX + new String(encodedBytes);
    }

}
