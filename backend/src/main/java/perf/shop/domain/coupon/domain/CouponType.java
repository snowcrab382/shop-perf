package perf.shop.domain.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponType {
    FIRST_COMES_FIRST_SERVES("first_comes_first_serves"),
    ;

    private final String value;
}
