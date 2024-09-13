package perf.shop.domain.coupon.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.coupon.application.CouponService;
import perf.shop.domain.coupon.dto.request.CouponCreateRequest;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponApi {

    private final CouponService couponService;

    @PostMapping
    public ApiResponse<Void> createCoupon(@RequestBody @Valid CouponCreateRequest request) {
        couponService.createCoupon(request);
        return ApiResponse.of(ResponseCode.CREATED);
    }
}
