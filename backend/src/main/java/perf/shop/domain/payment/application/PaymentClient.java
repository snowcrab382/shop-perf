package perf.shop.domain.payment.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Random;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import perf.shop.domain.payment.domain.PaymentStatus;
import perf.shop.domain.payment.domain.PaymentType;
import perf.shop.domain.payment.dto.request.PaymentRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmFailedResponse;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.domain.payment.error.exception.PaymentConfirmErrorCode;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.global.config.properties.PaymentProperties;

@Component
public class PaymentClient {

    private static final String BASIC_DELIMITER = ":";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final int CONNECT_TIMEOUT_SECONDS = 1;
    private static final int READ_TIMEOUT_SECONDS = 30;

    private final ObjectMapper objectMapper;
    private final PaymentProperties paymentProperties;
    private final RestClient restClient;

    public PaymentClient(PaymentProperties paymentProperties,
                         ObjectMapper objectMapper) {
        this.paymentProperties = paymentProperties;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .requestFactory(createPaymentRequestFactory())
                .requestInterceptor(new PaymentExceptionInterceptor())
                .defaultHeader(HttpHeaders.AUTHORIZATION, createPaymentAuthHeader(paymentProperties))
                .build();
    }

    public PaymentConfirmResponse confirmPayment(PaymentRequest paymentRequest) {
        return restClient
                .post()
                .uri(paymentProperties.getConfirmUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new PaymentConfirmFailedException(getPaymentConfirmErrorCode(response));
                })
                .body(PaymentConfirmResponse.class);
    }

    public PaymentConfirmResponse fakeConfirmPayment(PaymentRequest paymentRequest) {
        ZonedDateTime requestAt = ZonedDateTime.now();
        try {
            restClient
                    .get()
                    .uri("http://www.google.com")
                    .retrieve()
                    .toBodilessEntity();
            long delay = 1000 + new Random().nextInt(1001);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
        return PaymentConfirmResponse.builder()
                .paymentKey(paymentRequest.getPaymentKey())
                .orderId(paymentRequest.getOrderId())
                .orderName("testOrderName")
                .totalAmount(paymentRequest.getAmount())
                .requestedAt(requestAt)
                .approvedAt(ZonedDateTime.now())
                .type(PaymentType.CARD)
                .status(PaymentStatus.DONE)
                .build();
    }

    private ClientHttpRequestFactory createPaymentRequestFactory() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS))
                .withReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS));

        return ClientHttpRequestFactories.get(SimpleClientHttpRequestFactory.class, settings);
    }

    private String createPaymentAuthHeader(PaymentProperties paymentProperties) {
        byte[] encodedBytes = Base64.getEncoder().encode((paymentProperties.getSecretKey() + BASIC_DELIMITER).getBytes(
                StandardCharsets.UTF_8));
        return AUTH_HEADER_PREFIX + new String(encodedBytes);
    }

    private PaymentConfirmErrorCode getPaymentConfirmErrorCode(ClientHttpResponse response) throws IOException {
        PaymentConfirmFailedResponse failedResponse = objectMapper.readValue(
                response.getBody(), PaymentConfirmFailedResponse.class);
        return PaymentConfirmErrorCode.findByName(failedResponse.getCode());
    }
}
