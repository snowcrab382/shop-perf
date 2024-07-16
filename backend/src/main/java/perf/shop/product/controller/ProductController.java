package perf.shop.product.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.product.domain.RequestDto;
import perf.shop.product.domain.ResponseDto;

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
        return ApiResponse.ofNoContent(ResponseCode.GET);
    }
}
