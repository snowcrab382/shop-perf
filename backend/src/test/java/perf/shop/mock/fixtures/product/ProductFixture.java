package perf.shop.mock.fixtures.product;

import perf.shop.domain.product.domain.Product;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.domain.product.dto.response.ProductFindByIdResponse;

public class ProductFixture {

    public static Product createProduct(Long categoryId, Long sellerId, String name, String description, Long price,
                                        Long stock) {
        return Product.builder()
                .categoryId(categoryId)
                .sellerId(sellerId)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();
    }

    public static ProductSaveRequest createProductSaveRequest(String name, String description, Long price, Long stock,
                                                              Long categoryId) {
        return ProductSaveRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .categoryId(categoryId)
                .build();
    }

    public static ProductFindByIdResponse createProductFindByIdResponse(String name, Long price, String image,
                                                                        String description, Long stock) {
        return ProductFindByIdResponse.builder()
                .name(name)
                .price(price)
                .image(image)
                .description(description)
                .stock(stock)
                .build();
    }
}
