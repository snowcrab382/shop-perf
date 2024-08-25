package perf.shop.domain.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.model.dto.request.ShippingInfoRequest;
import perf.shop.domain.payment.dto.request.PaymentRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRequest {

    @Valid
    @NotNull
    private OrdererRequest orderer;

    @Valid
    @NotNull
    private ShippingInfoRequest shippingInfo;

    @Valid
    @NotNull
    private List<OrderLineRequest> orderLines;

    @Valid
    @NotNull
    private PaymentRequest paymentInfo;

    @Builder
    private OrderRequest(OrdererRequest orderer, ShippingInfoRequest shippingInfo,
                         List<OrderLineRequest> orderLines,
                         PaymentRequest paymentInfo) {
        this.orderer = orderer;
        this.shippingInfo = shippingInfo;
        this.orderLines = orderLines;
        this.paymentInfo = paymentInfo;
    }
}

