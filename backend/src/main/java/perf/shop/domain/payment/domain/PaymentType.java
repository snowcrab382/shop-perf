package perf.shop.domain.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {
    TOSS("toss");

    private final String value;
}
