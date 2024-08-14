package perf.shop.domain.cart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
import perf.shop.domain.cart.dto.response.CartProductResponse;
import perf.shop.domain.cart.repository.CartProductRepository;
import perf.shop.domain.cart.repository.CartRepository;
import perf.shop.domain.product.dao.ProductRepository;
import perf.shop.domain.product.domain.Product;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.ErrorCode;

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

    Cart createCart(Long userId) {
        return Cart.builder()
                .userId(userId)
                .build();
    }

    Product createProduct(Long categoryId, Long sellerId, String name, String description, Long price, Long stock) {
        return Product.builder()
                .categoryId(categoryId)
                .sellerId(sellerId)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();
    }

    @Nested
    @DisplayName("장바구니 상품 목록 조회 테스트")
    class GetCartProducts {

        CartProduct createCartProduct(Cart cart, Product product, Integer quantity) {
            return CartProduct.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }

        @Test
        @DisplayName("성공 - 장바구니에 상품이 없으면 빈 목록 반환")
        void getCartProducts_success_returnEmptyList() throws Exception {
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
        void getCartProducts_success_returnCartProducts() throws Exception {
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
        void addProduct_success_addNewProductToCart() throws Exception {
            // given
            AddProductRequest addProductRequest = createAddProductRequest(1L, 10);
            Cart cart = createCart(1L);
            Product product = createProduct(1L, 1L, "상품1", "상품1 설명", 1000L, 10L);

            given(productRepository.findById(any())).willReturn(Optional.of(product));
            given(cartRepository.findByUserId(any())).willReturn(Optional.of(cart));

            // when
            cartService.addProduct(addProductRequest, 1L);

            // then
            assertThat(cart.getCartProducts().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품이면 예외 발생")
        void addProduct_throwException_ifProductNotExists() throws Exception {
            // given
            AddProductRequest addProductRequest = createAddProductRequest(1L, 10);
            given(productRepository.findById(any())).willThrow(
                    new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> cartService.addProduct(addProductRequest, 1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PRODUCT_NOT_FOUND);
        }

        AddProductRequest createAddProductRequest(Long productId, Integer quantity) {
            return AddProductRequest.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build();
        }

    }
}
