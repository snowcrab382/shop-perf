package perf.shop.mock.fixtures.payment;

import perf.shop.domain.payment.dto.request.PaymentRequest;

public class PaymentFixture {

    public static PaymentRequest createPaymentRequest(String paymentKey, String orderId, Long amount) {
        return PaymentRequest.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .amount(amount)
                .build();
    }

}
