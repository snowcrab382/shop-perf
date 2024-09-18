package perf.shop.domain.coupon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.coupon.dao.UserCouponRepository;
import perf.shop.domain.coupon.domain.UserCoupon;
import perf.shop.domain.coupon.dto.response.UserCouponResponse;
import perf.shop.domain.user.UserRepository;
import perf.shop.domain.user.domain.User;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserCouponResponse> getUserCoupons(Long userId) {
        User user = findUser(userId);
        List<UserCoupon> coupons = userCouponRepository.findUsableCouponsByUser(user);
        return coupons.stream().map(UserCouponResponse::from).toList();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.USER_NOT_FOUND));
    }
}
