package perf.shop.domain.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.product.domain.Product;

@Getter
public class ProductFindByIdResponse {

    private String name;
    private Long price;
    private String image;
    private String description;
    private Long stock;

    @Builder
    private ProductFindByIdResponse(String name, Long price, String image, String description, Long stock) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.stock = stock;
    }

    public static ProductFindByIdResponse of(Product product) {
        return ProductFindByIdResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .image(product.getImage())
                .description(product.getDescription())
                .stock(product.getStock())
                .build();
    }

}
