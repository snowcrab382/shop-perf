package perf.shop.domain.cart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static perf.shop.mock.fixtures.cart.CartFixture.createAddProductRequest;
import static perf.shop.mock.fixtures.cart.CartFixture.createCart;
import static perf.shop.mock.fixtures.cart.CartFixture.createCartProduct;
import static perf.shop.mock.fixtures.cart.CartFixture.createUpdateProductRequest;
import static perf.shop.mock.fixtures.product.ProductFixture.createProduct;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perf.shop.domain.cart.domain.Cart;
import perf.shop.domain.cart.domain.CartProduct;
import perf.shop.domain.cart.dto.request.AddProductRequest;
import perf.shop.domain.cart.dto.request.UpdateProductRequest;
import perf.shop.domain.cart.dto.response.CartProductResponse;
import perf.shop.domain.cart.repository.CartProductRepository;
import perf.shop.domain.cart.repository.CartRepository;
import perf.shop.domain.product.dao.ProductRepository;
import perf.shop.domain.product.domain.Product;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] CartService")
class CartServiceTest {

    @InjectMocks
    CartService cartService;

    @Mock
    CartRepository cartRepository;

    @Mock
    CartProductRepository cartProductRepository;

    @Mock
    ProductRepository productRepository;

    @Nested
    @DisplayName("장바구니 상품 목록 조회 테스트")
    class GetCartProducts {

        @Test
        @DisplayName("성공 - 장바구니에 상품이 없으면 빈 목록 반환")
        void getCartProducts_success_returnEmptyList() {
            // given
            Long userId = 1L;
            Cart cart = createCart(userId);
            given(cartRepository.findByUserId(any())).willReturn(Optional.of(cart));

            // when
            List<CartProductResponse> cartProducts = cartService.getCartProducts(userId);

            // then
            assertThat(cartProducts.size()).isEqualTo(0);

        }

        @Test
        @DisplayName("성공 - 장바구니에 상품이 존재하면 목록 반환")
        void getCartProducts_success_returnCartProducts() {
            // given
            Long userId = 1L;
            Cart cart = createCart(userId);
            Product product = createProduct(1L, 1L, "상품1", "상품1 설명", 1000L, 10L);
            CartProduct cartProduct = createCartProduct(cart, product, 2);
            cart.addProduct(cartProduct);
            given(cartRepository.findByUserId(any())).willReturn(Optional.of(cart));

            // when
            List<CartProductResponse> cartProducts = cartService.getCartProducts(userId);

            // then
            assertThat(cartProducts.size()).isEqualTo(1);

        }

    }

    @Nested
    @DisplayName("장바구니 상품 등록 테스트")
    class AddProduct {

        @Test
        @DisplayName("성공 - 새로운 상품이면 장바구니에 추가")
        void addProduct_success_addNewProductToCart() {
            // given
            Long userId = 1L;
            AddProductRequest addProductRequest = createAddProductRequest(1L, 10);
            Cart cart = createCart(userId);
            Product product = createProduct(1L, 1L, "상품1", "상품1 설명", 1000L, 10L);

            given(productRepository.findById(any())).willReturn(Optional.of(product));
            given(cartRepository.findByUserId(any())).willReturn(Optional.of(cart));

            // when
            cartService.addProduct(addProductRequest, userId);

            // then
            assertThat(cart.getCartProducts().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품이면 예외 발생")
        void addProduct_throwException_ifProductNotExists() {
            // given
            Long userId = 1L;
            AddProductRequest addProductRequest = createAddProductRequest(1L, 10);
            given(productRepository.findById(any())).willThrow(
                    new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> cartService.addProduct(addProductRequest, userId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("장바구니 상품 수정 테스트")
    class UpdateProduct {

        @Test
        @DisplayName("성공")
        void updateProduct_success() {
            // given
            Long userId = 1L;
            Long cartProductId = 1L;
            UpdateProductRequest updateProductRequest = createUpdateProductRequest(cartProductId, 10);

            Cart cart = createCart(userId);
            Product product = createProduct(1L, 1L, "상품1", "상품1 설명", 1000L, 10L);
            CartProduct cartProduct = createCartProduct(cart, product, 5);
            cart.addProduct(cartProduct);
            given(cartProductRepository.findById(any())).willReturn(Optional.of(cartProduct));

            // when
            cartService.updateProduct(cartProductId, updateProductRequest);

            // then
            assertThat(cartProduct.getQuantity()).isEqualTo(10);
        }

        @Test
        @DisplayName("실패 - 장바구니 상품이 존재하지 않으면 예외 발생")
        void updateProduct_throwException_IfCartProductNotExists() {
            // given
            Long cartProductId = 1L;
            UpdateProductRequest updateProductRequest = createUpdateProductRequest(1L, 10);
            given(cartProductRepository.findById(any())).willThrow(
                    new EntityNotFoundException(GlobalErrorCode.CART_PRODUCT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> cartService.updateProduct(cartProductId, updateProductRequest))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.CART_PRODUCT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("장바구니 상품 삭제 테스트")
    class DeleteProduct {

        @Test
        @DisplayName("성공")
        void deleteProduct_success() {
            // given
            Long cartProductId = 1L;
            Long userId = 1L;
            Cart cart = createCart(userId);
            Product product = createProduct(1L, 1L, "상품1", "상품1 설명", 1000L, 10L);
            CartProduct cartProduct = createCartProduct(cart, product, 5);
            given(cartRepository.findByUserId(any())).willReturn(Optional.of(cart));
            given(cartProductRepository.findByIdAndCartId(cartProductId, cart.getId())).willReturn(
                    Optional.of(cartProduct));

            // when
            cartService.deleteProduct(cartProductId, userId);

            // then
            then(cartProductRepository).should().delete(cartProduct);
        }

        @Test
        @DisplayName("실패 - 장바구니에 해당 상품이 존재하지 않으면 예외 발생")
        void deleteProduct_throwException_ifProductNotExistsInCart() {
            // given
            Long userId = 1L;
            Long cartProductId = 1L;
            Cart cart = createCart(userId);
            given(cartRepository.findByUserId(any())).willReturn(Optional.of(cart));
            given(cartProductRepository.findByIdAndCartId(cartProductId, cart.getId())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> cartService.deleteProduct(cartProductId, userId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.CART_PRODUCT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("장바구니 상품 전체 삭제 테스트")
    class DeleteAllCartProducts {

        @Test
        @DisplayName("성공")
        void deleteAllCartProducts_success() {
            // given
            Long userId = 1L;
            Cart cart = createCart(userId);
            given(cartRepository.findByUserId(any())).willReturn(Optional.of(cart));

            // when
            cartService.deleteAllCartProducts(userId);

            // then
            then(cartProductRepository).should().deleteAllByCartId(cart.getId());

        }
    }

}
