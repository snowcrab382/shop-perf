package perf.shop.domain.product.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.product.dto.RequestDto;
import perf.shop.domain.product.dto.ResponseDto;
import perf.shop.domain.product.exception.ProductNotFoundException;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
public class ProductController {

    @GetMapping("/test")
    public ApiResponse<ResponseDto> test(@Valid RequestDto request) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(request.getId());
        responseDto.setName(request.getName());
        return ApiResponse.of(ResponseCode.GET, responseDto);
    }

    @GetMapping("/test2")
    public ApiResponse<String> test2(@Valid RequestDto request) {
        return ApiResponse.of(ResponseCode.GET, "test");
    }

    @GetMapping("/test3")
    public ApiResponse<Void> test3(@Valid RequestDto request) {
        return ApiResponse.of(ResponseCode.GET);
    }

    @GetMapping("/test4")
    public ApiResponse<Void> test4() {
        throw new ProductNotFoundException();
    }

}
