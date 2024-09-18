package perf.shop.domain.coupon.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.coupon.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
