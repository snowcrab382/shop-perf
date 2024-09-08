package perf.shop.domain.payment.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import perf.shop.domain.payment.domain.PaymentStatus;
import perf.shop.domain.payment.domain.PaymentType;
import perf.shop.domain.payment.dto.request.PaymentApproveRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmFailedResponse;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.domain.payment.error.PaymentExceptionInterceptor;
import perf.shop.domain.payment.error.exception.PaymentConfirmErrorCode;
import perf.shop.domain.payment.error.exception.PaymentConfirmFailedException;
import perf.shop.domain.payment.error.exception.PaymentConfirmTemporaryFailedException;
import perf.shop.domain.payment.error.exception.PaymentStatusUncertainException;
import perf.shop.global.config.properties.PaymentProperties;

@Slf4j
@Component
public class PaymentClient {

    private static final String BASIC_DELIMITER = ":";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final String IDEMPOTENT_HEADER = "Idempotency-Key";
    private static final int CONNECT_TIMEOUT_SECONDS = 1;
    private static final int READ_TIMEOUT_SECONDS = 3;

    private final PaymentProperties paymentProperties;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public PaymentClient(PaymentProperties paymentProperties, ObjectMapper objectMapper) {
        this.paymentProperties = paymentProperties;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .requestFactory(createPaymentRequestFactory())
                .requestInterceptor(new PaymentExceptionInterceptor())
                .defaultHeader(HttpHeaders.AUTHORIZATION, createPaymentAuthHeader(paymentProperties))
                .build();
    }

    @Retryable(
            retryFor = {
                    PaymentConfirmTemporaryFailedException.class,
                    ConnectException.class,
                    SocketTimeoutException.class
            },
            backoff = @Backoff(delay = 1000, maxDelay = 3000, multiplier = 2),
            recover = "recoverPaymentConfirm"
    )
    public PaymentConfirmResponse confirmPayment(PaymentApproveRequest paymentApproveRequest) {
        return restClient
                .post()
                .uri(paymentProperties.getConfirmUrl())
                .header(HttpHeaders.AUTHORIZATION, createPaymentAuthHeader(paymentProperties))
                .header(IDEMPOTENT_HEADER, paymentApproveRequest.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentApproveRequest)
                .retrieve()
                // 구매자에 대한 사유로 발생한 에러
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new PaymentConfirmFailedException(
                            getPaymentConfirmErrorCode(response));
                })
                // 결제 승인 서버에서 발생한 일시적인 에러. 재시도 수행
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new PaymentConfirmTemporaryFailedException(
                            getPaymentConfirmErrorCode(response));
                })
                .body(PaymentConfirmResponse.class);
    }

    @Recover
    public PaymentConfirmResponse recoverPaymentConfirm(Exception e, PaymentApproveRequest paymentApproveRequest) {
        throw new PaymentStatusUncertainException(PaymentConfirmErrorCode.PAYMENT_STATUS_UNCERTAIN);
    }

    public PaymentConfirmResponse fakeConfirmPayment(PaymentApproveRequest paymentApproveRequest) {
        PaymentConfirmResponse fakeResponse = PaymentConfirmResponse.builder()
                .paymentKey(paymentApproveRequest.getPaymentKey())
                .orderId(paymentApproveRequest.getOrderId())
                .orderName("testOrderName")
                .totalAmount(paymentApproveRequest.getAmount())
                .requestedAt(ZonedDateTime.now())
                .approvedAt(ZonedDateTime.now())
                .type(PaymentType.CARD)
                .status(PaymentStatus.DONE)
                .build();

        int delay = 1000;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }

        return fakeResponse;
    }

//    public PaymentConfirmResponse checkPaymentByOrderId(String orderId) {
//        return restClient
//                .get()
//                .uri(paymentProperties.getCheckUrl() + orderId)
//                .header(HttpHeaders.AUTHORIZATION, createPaymentAuthHeader(paymentProperties))
//                .retrieve()
//                .body(PaymentConfirmResponse.class)
//                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
//                .doOnError(throwable -> {
//                    WebClientResponseException exception = (WebClientResponseException) throwable;
//                    log.error("주문 번호 {} 에 대한 결제 확인 중 오류 발생 : {}", orderId,
//                            exception.getResponseBodyAsString());
//                    throw new PaymentConfirmFailedException(PaymentConfirmErrorCode.findByName(exception.getMessage()));
//                })
//                .block();
//    }

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
        PaymentConfirmFailedResponse confirmFailedResponse = objectMapper.readValue(
                response.getBody(), PaymentConfirmFailedResponse.class);
        return PaymentConfirmErrorCode.findByName(confirmFailedResponse.getCode());
    }
}
