package perf.shop.domain.order.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.order.application.OrdersService;
import perf.shop.domain.order.dto.request.OrderCreateRequest;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersApi {

    private final OrdersService ordersService;

    @PostMapping
    public ApiResponse<Void> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest,
                                         @UserId Long userId) {
        ordersService.createOrder(userId, orderCreateRequest);
        return ApiResponse.of(ResponseCode.CREATED);
    }
}
