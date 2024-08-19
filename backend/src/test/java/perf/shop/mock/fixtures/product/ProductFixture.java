package perf.shop.mock.fixtures.product;

import perf.shop.domain.product.domain.Product;

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
}
