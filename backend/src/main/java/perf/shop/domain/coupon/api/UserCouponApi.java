package perf.shop.domain.coupon.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.coupon.application.UserCouponService;
import perf.shop.domain.coupon.dto.response.UserCouponResponse;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/users/coupons")
@RequiredArgsConstructor
public class UserCouponApi {

    private final UserCouponService userCouponService;

    @GetMapping
    public ApiResponse<List<UserCouponResponse>> getUserCoupons(@UserId Long userId) {
        return ApiResponse.of(ResponseCode.GET, userCouponService.getUserCoupons(userId));
    }
}
