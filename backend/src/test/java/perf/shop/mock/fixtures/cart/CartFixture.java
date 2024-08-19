package perf.shop.mock.fixtures.cart;

import perf.shop.domain.cart.domain.Cart;
import perf.shop.domain.cart.domain.CartProduct;
import perf.shop.domain.cart.dto.request.AddProductRequest;
import perf.shop.domain.cart.dto.request.UpdateProductRequest;
import perf.shop.domain.cart.dto.response.CartProductResponse;
import perf.shop.domain.product.domain.Product;

public class CartFixture {

    public static UpdateProductRequest createUpdateProductRequest(Long productId, Integer quantity) {
        return UpdateProductRequest.builder()
                .cartProductId(productId)
                .quantity(quantity)
                .build();
    }

    public static CartProductResponse createCartProductResponse(Long id, String name, String image, Long price,
                                                                Integer quantity) {
        return CartProductResponse.builder()
                .cartProductId(id)
                .productId(id)
                .productName(name)
                .image(image)
                .price(price)
                .quantity(quantity)
                .build();
    }

    public static AddProductRequest createAddProductRequest(Long productId, Integer quantity) {
        return AddProductRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }

    public static Cart createCart(Long userId) {
        return Cart.builder()
                .userId(userId)
                .build();
    }

    public static CartProduct createCartProduct(Cart cart, Product product, Integer quantity) {
        return CartProduct.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();
    }
}
