package perf.shop.domain.cart.service;

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
import perf.shop.global.error.exception.GlobalErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    private static void addProductToCartByRequest(AddProductRequest addProductRequest, Cart cart, Product product) {
        cart.getCartProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().getId().equals(addProductRequest.getProductId()))
                .findAny()
                .ifPresentOrElse(
                        cartProduct -> cartProduct.addQuantity(addProductRequest.getQuantity()),
                        () -> cart.addProduct(CartProduct.from(addProductRequest, cart, product))
                );
    }

    public List<CartProductResponse> getCartProducts(Long userId) {
        return getCart(userId).getCartProducts().stream()
                .map(CartProductResponse::from)
                .toList();
    }

    public void addProduct(AddProductRequest addProductRequest, Long userId) {
        Product product = productRepository.findById(addProductRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = getCart(userId);
        addProductToCartByRequest(addProductRequest, cart, product);
    }

    public void updateProduct(Long cartProductId, UpdateProductRequest updateProductRequest) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.CART_PRODUCT_NOT_FOUND));

        cartProduct.updateQuantity(updateProductRequest.getQuantity());
    }

    public void deleteProduct(Long cartProductId, Long userId) {
        Cart cart = getCart(userId);
        CartProduct cartProduct = cartProductRepository.findByIdAndCartId(cartProductId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.CART_PRODUCT_NOT_FOUND));

        cartProductRepository.delete(cartProduct);
    }

    public void deleteAllCartProducts(Long userId) {
        Cart cart = getCart(userId);
        cartProductRepository.deleteAllByCartId(cart.getId());
    }

    private Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.of(userId)));
    }

}
