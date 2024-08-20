package perf.shop.mock.fixtures.payment;

import perf.shop.domain.order.domain.Order;
import perf.shop.domain.payment.domain.Payment;
import perf.shop.domain.payment.domain.PaymentInfo;
import perf.shop.domain.payment.domain.PaymentMethod;
import perf.shop.domain.payment.domain.PaymentType;

public class PaymentFixture {

    public static PaymentInfo createPaymentInfo(String method, String type) {
        return PaymentInfo.builder()
                .method(PaymentMethod.valueOf(method))
                .type(PaymentType.valueOf(type))
                .build();
    }

    public static Payment createPayment(Order order, PaymentInfo paymentInfo) {
        return Payment.builder()
                .order(order)
                .paymentInfo(paymentInfo)
                .build();
    }
}
