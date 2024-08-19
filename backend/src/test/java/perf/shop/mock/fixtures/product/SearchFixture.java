package perf.shop.mock.fixtures.product;

import perf.shop.domain.product.dto.request.SearchCondition;
import perf.shop.domain.product.dto.response.ProductSearchResponse;

public class SearchFixture {

    public static ProductSearchResponse createProductSearchResponse(Long id, String name, Long price, String image) {
        return ProductSearchResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .image(image)
                .build();
    }

    public static SearchCondition createSearchCondition(Long categoryId, String keyword, String sorter) {
        return SearchCondition.builder()
                .categoryId(categoryId)
                .keyword(keyword)
                .sorter(sorter)
                .build();
    }
}
