package perf.shop.domain.order.dto.request;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingInfoRequest {

    @Valid
    private AddressRequest address;

    @Valid
    private ReceiverRequest receiver;

    @Builder
    private ShippingInfoRequest(AddressRequest address, ReceiverRequest receiver) {
        this.address = address;
        this.receiver = receiver;
    }
}
