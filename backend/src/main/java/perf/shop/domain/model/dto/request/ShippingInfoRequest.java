package perf.shop.domain.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingInfoRequest {

    @Valid
    @NotNull
    private AddressRequest address;

    @Valid
    @NotNull
    private ReceiverRequest receiver;

    @Builder
    private ShippingInfoRequest(AddressRequest address, ReceiverRequest receiver) {
        this.address = address;
        this.receiver = receiver;
    }
}
