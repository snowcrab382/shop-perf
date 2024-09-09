package perf.shop.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static perf.shop.mock.fixtures.product.ProductFixture.createProduct;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.product.dao.ProductRepository;
import perf.shop.domain.product.domain.Product;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.domain.product.dto.response.ProductFindByIdResponse;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;
import perf.shop.mock.fixtures.product.ProductFixture;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] ProductService")
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    CategoryService categoryService;

    @Mock
    ProductRepository productRepository;

    @Nested
    @DisplayName("상품 저장 테스트")
    class SaveProduct {

        @Test
        @DisplayName("성공")
        void saveProduct_saved_ifCategoryIdExists() {
            // given
            Long sellerId = 1L;
            Product newProduct = createProduct(1L, 1L, "상품명", "상품 설명", 10000L, 10L);
            ProductSaveRequest productSaveRequest = ProductFixture.createProductSaveRequest("상품명", "상품 설명", 10000L, 10L,
                    1L);
            given(productRepository.save(any(Product.class))).willReturn(newProduct);

            // when
            productService.saveProduct(productSaveRequest, sellerId);

            // then
            then(productRepository).should().save(any(Product.class));

        }

        @Test
        @DisplayName("실패 - 카테고리가 존재하지 않으면 예외 발생")
        void saveProduct_throwException_ifCategoryIdNotExists() {
            // given
            Long sellerId = 1L;
            ProductSaveRequest productSaveRequest = ProductFixture.createProductSaveRequest("상품명", "상품 설명", 10000L, 10L,
                    1L);
            doThrow(new EntityNotFoundException(GlobalErrorCode.CATEGORY_NOT_FOUND))
                    .when(categoryService).validateCategoryExistsById(any(Long.class));

            // when & then
            assertThatThrownBy(() -> productService.saveProduct(productSaveRequest, sellerId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("상품 조회 테스트")
    class FindProductById {

        @Test
        @DisplayName("성공")
        void findProductById_success_ifProductExists() {
            // given
            Long productId = 1L;
            Product product = createProduct(1L, 10000L, "상품명", "상품 설명", 1L, 10L);
            given(productRepository.findById(productId)).willReturn(Optional.ofNullable(product));

            // when
            ProductFindByIdResponse productFindById = productService.findProductById(productId);

            // then
            then(productRepository).should().findById(productId);
            assertThat(productFindById).isInstanceOf(ProductFindByIdResponse.class);
            assertThat(productFindById.getName()).isEqualTo(product.getName());
            assertThat(productFindById.getPrice()).isEqualTo(product.getPrice());
            assertThat(productFindById.getDescription()).isEqualTo(product.getDescription());
            assertThat(productFindById.getStock()).isEqualTo(product.getStock());
        }

        @Test
        @DisplayName("실패 - 상품이 존재하지 않으면 예외 발생")
        void findProductById_throwException_ifProductNotExists() {
            // given
            Long productId = 1L;
            given(productRepository.findById(productId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> productService.findProductById(productId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.PRODUCT_NOT_FOUND);

        }
    }

}
