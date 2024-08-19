package perf.shop.mock.fixtures.product;

import perf.shop.domain.product.domain.Category;
import perf.shop.domain.product.dto.response.CategoryResponse;

public class CategoryFixture {

    public static CategoryResponse createCategoryResponse(Long id, String name) {
        return CategoryResponse
                .builder()
                .id(id)
                .name(name)
                .build();
    }

    public static Category createCategory(Long id, String name) {
        return Category
                .builder()
                .id(id)
                .name(name)
                .build();
    }
}
