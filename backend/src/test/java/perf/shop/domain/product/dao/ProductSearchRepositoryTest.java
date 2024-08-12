package perf.shop.domain.product.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import perf.shop.domain.product.domain.Product;
import perf.shop.domain.product.dto.request.SearchCondition;
import perf.shop.domain.product.dto.request.Sort;
import perf.shop.domain.product.dto.response.ProductSearchResponse;
import perf.shop.global.config.TestQuerydslConfig;

@DataJpaTest
@Import(TestQuerydslConfig.class)
@DisplayName("[단위 테스트] ProductSearchRepository")
class ProductSearchRepositoryTest {

    @Autowired
    ProductSearchRepository productSearchRepository;

    @Autowired
    ProductRepository productRepository;

    private Product createProduct(Long categoryId, Long sellerId, String name, String description, Long price,
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

    private SearchCondition createSearchCondition(Long categoryId, String keyword, String sorter) {
        return SearchCondition.builder()
                .categoryId(categoryId)
                .keyword(keyword)
                .sorter(sorter)
                .build();
    }

    @BeforeEach
    void setUp() {
        productRepository.save(createProduct(1L, 1L, "상품1", "상품1 설명", 1000L, 10L));
        productRepository.save(createProduct(2L, 1L, "상품2 키워드", "상품2 설명", 2000L, 20L));
        productRepository.save(createProduct(3L, 1L, "상품3", "상품3 설명", 3000L, 30L));
        productRepository.save(createProduct(4L, 1L, "상품4", "상품4 설명", 4000L, 40L));
    }

    @Nested
    @DisplayName("상품 목록 동적 쿼리 검색 테스트")
    class Search {

        @Test
        @DisplayName("입력 조건에 카테고리id가 존재할 경우 where절에 적용되어 반환")
        void search_ReturnResult_IfCategoryIdExistsInSearchCondition() {
            // given
            SearchCondition condition = createSearchCondition(1L, "상품", Sort.PRICEDESC.getValue());
            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<ProductSearchResponse> result = productSearchRepository.search(condition, pageable);

            // then
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("상품1");
        }

        @Test
        @DisplayName("입력 조건에 키워드가 존재할 경우 where절에 적용되어 반환")
        void search_ReturnResult_IfKeywordExistsInSearchCondition() {
            // given
            SearchCondition condition = createSearchCondition(null, "키워드", Sort.PRICEDESC.getValue());
            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<ProductSearchResponse> result = productSearchRepository.search(condition, pageable);

            // then
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("상품2 키워드");
        }

        @Test
        @DisplayName("입력 조건에 정렬 조건이 존재할 경우 order by절에 적용되어 반환")
        void search_ReturnResult_IfSorterExistsInSearchCondition() {
            // given
            SearchCondition condition = createSearchCondition(null, "상품", Sort.PRICEDESC.getValue());
            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<ProductSearchResponse> result = productSearchRepository.search(condition, pageable);

            // then
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(4);
            assertThat(result.getContent().get(0).getName()).isEqualTo("상품4");
            assertThat(result.getContent().get(1).getName()).isEqualTo("상품3");
        }

        @Test
        @DisplayName("페이지 관련 정보도 함께 반환")
        void search_ReturnPageInfo() {
            // given
            SearchCondition condition = createSearchCondition(null, null, Sort.LATEST.getValue());
            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<ProductSearchResponse> result = productSearchRepository.search(condition, pageable);

            // then
            assertThat(result.getTotalPages()).isEqualTo(2);
            assertThat(result.getTotalElements()).isEqualTo(4);
            assertThat(result.getNumber()).isEqualTo(0);
            assertThat(result.getSize()).isEqualTo(2);
        }

        @Test
        @DisplayName("검색 조건 중 where과 order by에 대한 조건이 모두 null인 경우 예외 발생")
        void search_ThrowException_IfWhereAndOrderByIsNull() {
            // given
            SearchCondition condition = createSearchCondition(null, null, null);
            Pageable pageable = PageRequest.of(0, 2);

            // when & then
            assertThatThrownBy(() -> productSearchRepository.search(condition, pageable))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
