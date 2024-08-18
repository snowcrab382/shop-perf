package perf.shop.domain.order.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class ShippingInfoRequest {
    
    @Valid
    private AddressRequest address;

    @Valid
    private ReceiverRequest receiver;
}
