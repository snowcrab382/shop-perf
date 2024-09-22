package perf.shop.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineRequest {

    @NotNull
    private long productId;

    @NotNull
    @Min(1)
    private int quantity;

    @NotNull
    private long price;

    @Builder
    private OrderLineRequest(long productId, int quantity, long price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
}
