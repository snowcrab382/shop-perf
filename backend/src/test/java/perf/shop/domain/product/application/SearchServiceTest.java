package perf.shop.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import perf.shop.domain.product.dao.ProductSearchRepository;
import perf.shop.domain.product.dto.request.SearchCondition;
import perf.shop.domain.product.dto.response.ProductSearchResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] SearchService")
class SearchServiceTest {

    @InjectMocks
    SearchService searchService;

    @Mock
    ProductSearchRepository productSearchRepository;

    private SearchCondition createSearchCondition(Long categoryId, String keyword, String sorter) {
        return SearchCondition.builder()
                .categoryId(categoryId)
                .keyword(keyword)
                .sorter(sorter)
                .build();
    }

    @Nested
    @DisplayName("상품 검색 테스트")
    class Search {

        @Test
        @DisplayName("성공")
        void search_success() {
            // given
            SearchCondition condition = createSearchCondition(1L, "키워드", "sorter");
            Pageable pageable = PageRequest.of(0, 10);
            Page<ProductSearchResponse> productSearchResponses = Page.empty();
            given(productSearchRepository.search(condition, pageable)).willReturn(productSearchResponses);

            // when
            Page<ProductSearchResponse> result = searchService.search(condition, pageable);

            // then
            assertThat(result).isEqualTo(productSearchResponses);
            then(productSearchRepository).should().search(condition, pageable);
        }

        @Test
        @DisplayName("실패 - 검색 조건이 모두 null이면 예외 발생")
        void search_throwException_ifSearchConditionIsNull() {
            // given
            SearchCondition condition = createSearchCondition(null, null, null);
            Pageable pageable = PageRequest.of(0, 10);
            given(productSearchRepository.search(condition, pageable)).willThrow(NullPointerException.class);

            // when & then
            assertThatThrownBy(() -> searchService.search(condition, pageable))
                    .isInstanceOf(NullPointerException.class);
        }
    }

}
