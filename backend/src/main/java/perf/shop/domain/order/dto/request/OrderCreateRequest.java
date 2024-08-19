package perf.shop.domain.order.dto.request;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreateRequest {

    @Valid
    private OrdererRequest orderer;

    @Valid
    private ShippingInfoRequest shippingInfo;

    @Valid
    private List<OrderLineRequest> orderLines;

    @Builder
    private OrderCreateRequest(OrdererRequest orderer, ShippingInfoRequest shippingInfo,
                               List<OrderLineRequest> orderLines) {
        this.orderer = orderer;
        this.shippingInfo = shippingInfo;
        this.orderLines = orderLines;
    }
}

