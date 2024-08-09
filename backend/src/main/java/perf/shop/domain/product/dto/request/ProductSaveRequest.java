package perf.shop.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductSaveRequest {

    @NotNull
    private Long categoryId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Long price;

    @NotNull
    private Long stock;

}
