package perf.shop.domain.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.product.domain.Category;

@Getter
public class CategoryResponse {

    private Long id;
    private String name;

    @Builder
    private CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
