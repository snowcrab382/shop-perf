package perf.shop.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddProductRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

}
