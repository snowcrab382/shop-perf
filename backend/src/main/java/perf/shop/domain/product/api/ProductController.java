package perf.shop.domain.product.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.product.dto.RequestDto;
import perf.shop.domain.product.dto.ResponseDto;
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

}
