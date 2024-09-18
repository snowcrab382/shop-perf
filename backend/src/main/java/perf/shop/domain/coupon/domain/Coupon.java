package perf.shop.domain.coupon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.coupon.dto.request.CouponCreateRequest;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    private Integer discountAmount;

    private Integer discountPercent;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer remainingCount;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    private Coupon(CouponType couponType, DiscountType discountType, Integer discountAmount, Integer discountPercent,
                   Integer totalCount, LocalDateTime startedAt, LocalDateTime expiredAt) {
        this.couponType = couponType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.discountPercent = discountPercent;
        this.totalCount = totalCount;
        this.remainingCount = totalCount;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
    }

    public static Coupon from(CouponCreateRequest request) {
        return Coupon.builder()
                .couponType(CouponType.valueOf(request.getCouponType()))
                .discountType(DiscountType.valueOf(request.getDiscountType()))
                .discountAmount(request.getDiscountAmount())
                .discountPercent(request.getDiscountPercent())
                .totalCount(request.getTotalCount())
                .startedAt(request.getStartedAt())
                .expiredAt(request.getExpiredAt())
                .build();
    }
}
