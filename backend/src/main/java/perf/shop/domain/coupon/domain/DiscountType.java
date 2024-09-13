package perf.shop.domain.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType {
    AMOUNT("amount"),
    PERCENT("percent"),
    ;

    private final String value;

}
