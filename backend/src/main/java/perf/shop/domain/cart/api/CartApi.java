package perf.shop.domain.cart.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.cart.dto.request.AddProductRequest;
import perf.shop.domain.cart.dto.request.UpdateProductRequest;
import perf.shop.domain.cart.dto.response.CartProductResponse;
import perf.shop.domain.cart.service.CartService;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartApi {

    private final CartService cartService;

    @GetMapping
    public ApiResponse<List<CartProductResponse>> getCartProducts(@UserId Long userId) {
        return ApiResponse.of(ResponseCode.GET, cartService.getCartProducts(userId));
    }

    @PostMapping
    public ApiResponse<Void> addProduct(@RequestBody @Valid AddProductRequest addProductRequest,
                                        @UserId Long userId) {
        cartService.addProduct(addProductRequest, userId);
        return ApiResponse.of(ResponseCode.CREATED);
    }

    @PutMapping("/{cartProductId}")
    public ApiResponse<Void> updateProduct(@PathVariable("cartProductId") Long cartProductId,
                                           @RequestBody @Valid UpdateProductRequest updateProductRequest) {
        cartService.updateProduct(cartProductId, updateProductRequest);
        return ApiResponse.of(ResponseCode.UPDATED);
    }

    @DeleteMapping("/{cartProductId}")
    public ApiResponse<Void> deleteProduct(@PathVariable("cartProductId") Long cartProductId,
                                           @UserId Long userId) {
        cartService.deleteProduct(cartProductId, userId);
        return ApiResponse.of(ResponseCode.DELETED);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteAllCartProducts(@UserId Long userId) {
        cartService.deleteAllCartProducts(userId);
        return ApiResponse.of(ResponseCode.DELETED);
    }

}
