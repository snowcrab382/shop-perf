package perf.shop.domain.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import perf.shop.domain.cart.domain.CartProduct;

@Getter
public class CartProductResponse {

    private Long cartProductId;
    private Long productId;
    private String productName;
    private String image;
    private Long price;
    private Integer quantity;

    @Builder
    private CartProductResponse(Long cartProductId, Long productId, String productName, String image, Long price,
                                Integer quantity) {
        this.cartProductId = cartProductId;
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public static CartProductResponse from(CartProduct cartProduct) {
        return CartProductResponse.builder()
                .cartProductId(cartProduct.getId())
                .productId(cartProduct.getProduct().getId())
                .productName(cartProduct.getProduct().getName())
                .image(cartProduct.getProduct().getImage())
                .price(cartProduct.getProduct().getPrice())
                .quantity(cartProduct.getQuantity())
                .build();
    }
}
