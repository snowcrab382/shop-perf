package perf.shop.domain.product.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.product.application.SearchService;
import perf.shop.domain.product.dto.request.SearchCondition;
import perf.shop.domain.product.dto.response.ProductSearchResponse;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class SearchApi {

    private final SearchService searchService;

    @GetMapping("/search")
    public ApiResponse<Page<ProductSearchResponse>> searchByCondition(@Valid SearchCondition searchCondition,
                                                                      Pageable pageable) {
        return ApiResponse.of(ResponseCode.GET, searchService.search(searchCondition, pageable));
    }
}
