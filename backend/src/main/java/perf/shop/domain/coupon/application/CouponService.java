package perf.shop.domain.coupon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.coupon.dao.CouponRepository;
import perf.shop.domain.coupon.domain.Coupon;
import perf.shop.domain.coupon.dto.request.CouponCreateRequest;

@Transactional
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public void createCoupon(CouponCreateRequest request) {
        couponRepository.save(Coupon.from(request));
    }
}
