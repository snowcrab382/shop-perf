package perf.shop.domain.coupon.dto.response;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.coupon.domain.CouponType;
import perf.shop.domain.coupon.domain.DiscountType;
import perf.shop.domain.coupon.domain.UserCoupon;

@Getter
public class UserCouponResponse {

    private final Long id;

    private final CouponType couponType;

    private final DiscountType discountType;

    private final Integer discountAmount;

    private final Integer discountPercent;

    @Builder
    private UserCouponResponse(Long id, CouponType couponType, DiscountType discountType, Integer discountAmount,
                               Integer discountPercent) {
        this.id = id;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.discountPercent = discountPercent;
    }

    public static UserCouponResponse from(UserCoupon userCoupon) {
        return UserCouponResponse.builder()
                .id(userCoupon.getId())
                .couponType(userCoupon.getCoupon().getCouponType())
                .discountType(userCoupon.getCoupon().getDiscountType())
                .discountAmount(userCoupon.getCoupon().getDiscountAmount())
                .discountPercent(userCoupon.getCoupon().getDiscountPercent())
                .build();
    }
}
