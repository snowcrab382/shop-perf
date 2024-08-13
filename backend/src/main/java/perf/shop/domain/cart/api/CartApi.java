package perf.shop.domain.cart.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.cart.dto.request.AddProductRequest;
import perf.shop.domain.cart.service.CartService;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartApi {

    private final CartService cartService;

    @PostMapping
    public ApiResponse<Void> addProduct(@RequestBody @Valid AddProductRequest addProductRequest,
                                        @UserId Long userId) {
        cartService.addProduct(addProductRequest, userId);
        return ApiResponse.of(ResponseCode.CREATED);
    }

}
