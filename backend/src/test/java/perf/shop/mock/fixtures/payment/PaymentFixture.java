package perf.shop.mock.fixtures.payment;

import perf.shop.domain.payment.dto.request.PaymentApproveRequest;

public class PaymentFixture {

    public static PaymentApproveRequest createPaymentRequest(String paymentKey, String orderId, Long amount) {
        return PaymentApproveRequest.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .amount(amount)
                .build();
    }

}
