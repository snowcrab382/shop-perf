package perf.shop.domain.product.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.product.application.ProductService;
import perf.shop.domain.product.dto.request.ProductSaveRequest;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<Void> save(@RequestBody @Valid ProductSaveRequest productSaveRequest,
                                  @UserId Long sellerId) {
        productService.saveProduct(productSaveRequest, sellerId);
        return ApiResponse.of(ResponseCode.CREATED);
    }
}
