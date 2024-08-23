package perf.shop.domain.payment.dto.response;

import lombok.Getter;

@Getter
public class PaymentConfirmFailedResponse {

    private String code;
    private String message;
}
