package perf.shop.domain.product.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductSearchResponse {

    private Long id;
    private String name;
    private Long price;
    private String image;

    @QueryProjection
    @Builder
    public ProductSearchResponse(Long id, String name, Long price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }
}
