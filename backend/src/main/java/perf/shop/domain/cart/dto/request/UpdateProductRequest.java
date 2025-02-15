package perf.shop.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductRequest {

    @NotNull
    private Long cartProductId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @Builder
    private UpdateProductRequest(Long cartProductId, Integer quantity) {
        this.cartProductId = cartProductId;
        this.quantity = quantity;
    }

}
