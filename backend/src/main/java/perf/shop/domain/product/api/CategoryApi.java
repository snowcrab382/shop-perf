package perf.shop.domain.product.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.product.application.CategoryService;
import perf.shop.domain.product.dto.response.CategoryResponse;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryApi {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> findAll() {
        return ApiResponse.of(ResponseCode.GET, categoryService.findAll());
    }
}
