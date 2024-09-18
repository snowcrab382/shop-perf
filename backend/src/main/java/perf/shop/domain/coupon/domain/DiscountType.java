package perf.shop.domain.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType {
    AMOUNT("AMOUNT"),
    PERCENT("PERCENT"),
    ;

    private final String value;

}
