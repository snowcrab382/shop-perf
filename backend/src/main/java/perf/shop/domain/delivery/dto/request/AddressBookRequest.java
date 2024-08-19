package perf.shop.domain.delivery.dto.request;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressBookRequest {

    @Valid
    private ShippingInfoRequest shippingInfo;

    @Builder
    private AddressBookRequest(ShippingInfoRequest shippingInfo) {
        this.shippingInfo = shippingInfo;
    }
}
