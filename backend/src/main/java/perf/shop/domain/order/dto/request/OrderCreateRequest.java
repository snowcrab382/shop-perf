package perf.shop.domain.order.dto.request;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreateRequest {

    @Valid
    private OrdererRequest orderer;

    @Valid
    private ShippingInfoRequest shippingInfo;

    @Valid
    private List<OrderLineRequest> orderLines;
}

