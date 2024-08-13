package perf.shop.domain.cart.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import perf.shop.global.error.exception.ErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public List<CartProductResponse> getCartProducts(Long userId) {
        List<CartProduct> cartProducts = getCart(userId).getCartProducts();
        if (cartProducts.isEmpty()) {
            return Collections.emptyList();
        }

        return cartProducts.stream()
                .map(CartProductResponse::from)
                .toList();
    }

    public void addProduct(AddProductRequest addProductRequest, Long userId) {
        Product product = productRepository.findById(addProductRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = getCart(userId);
        cart.getCartProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().getId().equals(addProductRequest.getProductId()))
                .findAny()
                .ifPresentOrElse(
                        cartProduct -> cartProduct.addQuantity(addProductRequest.getQuantity()),
                        () -> cart.addProduct(CartProduct.from(addProductRequest, cart, product))
                );
    }

    public void updateProduct(Long cartProductId, UpdateProductRequest updateProductRequest, Long userId) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        cartProduct.updateQuantity(updateProductRequest.getQuantity());
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.of(userId)));
    }

}
