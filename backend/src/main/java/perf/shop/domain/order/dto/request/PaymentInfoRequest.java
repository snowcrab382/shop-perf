package perf.shop.domain.order.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.payment.domain.PaymentMethod;
import perf.shop.domain.payment.domain.PaymentType;
import perf.shop.global.annotation.EnumValue;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfoRequest {

    @EnumValue(enumClass = PaymentMethod.class, ignoreCase = true, message = "결제 수단이 올바르지 않습니다.")
    private String method;

    @EnumValue(enumClass = PaymentType.class, ignoreCase = true, message = "결제 타입이 올바르지 않습니다.")
    private String type;

    @Builder
    private PaymentInfoRequest(String method, String type) {
        this.method = method;
        this.type = type;
    }
}
