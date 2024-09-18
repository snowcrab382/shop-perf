package perf.shop.domain.coupon.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import perf.shop.domain.coupon.domain.UserCoupon;
import perf.shop.domain.user.domain.User;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Query("select uc from UserCoupon uc join fetch uc.coupon where uc.user = :user and uc.isUsed = false")
    List<UserCoupon> findUsableCouponsByUser(@Param("user") User user);
}
