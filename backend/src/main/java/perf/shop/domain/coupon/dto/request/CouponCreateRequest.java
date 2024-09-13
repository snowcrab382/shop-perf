package perf.shop.domain.coupon.dto.request;

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

    @NotNull
    private Integer amount;

    @NotNull
    private Integer totalCount;

    @NotNull
    @Min(1)
    private Integer maxCountPerUser;

    @NotNull
    private LocalDateTime startedAt;

    @NotNull
    private LocalDateTime expiredAt;
}
