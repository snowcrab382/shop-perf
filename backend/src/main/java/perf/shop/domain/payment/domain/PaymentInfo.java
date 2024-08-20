package perf.shop.domain.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.order.dto.request.PaymentInfoRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfo {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;

    @Builder
    private PaymentInfo(PaymentMethod method, PaymentType type) {
        this.method = method;
        this.type = type;
    }

    public static PaymentInfo from(PaymentInfoRequest request) {
        return PaymentInfo.builder()
                .method(PaymentMethod.valueOf(request.getMethod().toUpperCase()))
                .type(PaymentType.valueOf(request.getType().toUpperCase()))
                .build();
    }

}
