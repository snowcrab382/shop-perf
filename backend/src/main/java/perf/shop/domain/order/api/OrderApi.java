package perf.shop.domain.order.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.order.application.OrderFacadeService;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderFacadeService orderFacadeService;

    @PostMapping
    public ApiResponse<Void> order(@RequestBody @Valid OrderRequest orderRequest,
                                   @UserId Long userId) {
        orderFacadeService.order(userId, orderRequest);
        return ApiResponse.of(ResponseCode.ORDER_REQUEST_COMPLETE);
    }
}
