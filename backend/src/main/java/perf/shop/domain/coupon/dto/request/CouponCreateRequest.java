package perf.shop.domain.coupon.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.coupon.domain.CouponType;
import perf.shop.domain.coupon.domain.DiscountType;
import perf.shop.global.annotation.EnumValue;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCreateRequest {

    @EnumValue(enumClass = CouponType.class, ignoreCase = true)
    @NotBlank
    private String couponType;

    @EnumValue(enumClass = DiscountType.class, ignoreCase = true)
    @NotBlank
    private String discountType;

    @Min(100)
    private Integer discountAmount;

    @Min(1)
    @Max(100)
    private Integer discountPercent;

    @NotNull
    private Integer totalCount;

    @NotNull
    private LocalDateTime startedAt;

    @NotNull
    private LocalDateTime expiredAt;
}
