package perf.shop.domain.product.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] ProductService")
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    CategoryService categoryService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductSaveRequest productSaveRequest;

    @Nested
    @DisplayName("상품 저장 테스트")
    class SaveProduct {

        @Test
        @DisplayName("카테고리 검증에 성공하면 상품 저장 성공")
        void saveProduct_Saved_IfCategoryIdExists() {
            // given
            Long sellerId = 1L;
            Product newProduct = Product.of(productSaveRequest, sellerId);
            given(productRepository.save(any(Product.class))).willReturn(newProduct);

            // when
            productService.saveProduct(productSaveRequest, sellerId);

            // then
            then(productRepository).should().save(any(Product.class));

        }
    }

}
