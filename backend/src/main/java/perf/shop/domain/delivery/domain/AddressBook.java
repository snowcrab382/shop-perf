package perf.shop.domain.delivery.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.model.ShippingInfo;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "address_book")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Embedded
    @Column(nullable = false)
    private ShippingInfo shippingInfo;

    @Builder
    private AddressBook(Long userId, ShippingInfo shippingInfo) {
        this.userId = userId;
        this.shippingInfo = shippingInfo;
    }

    public static AddressBook of(Long userId, ShippingInfo shippingInfo) {
        return AddressBook.builder()
                .userId(userId)
                .shippingInfo(shippingInfo)
                .build();
    }

}
