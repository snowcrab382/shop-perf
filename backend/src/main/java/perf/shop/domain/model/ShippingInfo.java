package perf.shop.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingInfo {

    @Embedded
    private Address address;

    @Embedded
    private Receiver receiver;

    @Builder
    private ShippingInfo(Address address, Receiver receiver) {
        this.address = address;
        this.receiver = receiver;
    }

    public static ShippingInfo of(Address address, Receiver receiver) {
        return ShippingInfo.builder()
                .address(address)
                .receiver(receiver)
                .build();
    }
}
