package perf.shop.domain.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentConfirmFailedResponse {

    private final String code;
    private final String message;

    @Builder
    private PaymentConfirmFailedResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
