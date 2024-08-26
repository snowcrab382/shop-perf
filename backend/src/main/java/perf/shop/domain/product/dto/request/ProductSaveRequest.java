package perf.shop.domain.product.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSaveRequest {

    @NotNull
    private Long categoryId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Min(1)
    @Max(1000000)
    private Long price;

    @NotNull
    @Min(1)
//    @Max(100000)
    private Long stock;

    @Builder
    private ProductSaveRequest(Long categoryId, String name, String description, Long price, Long stock) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

}
