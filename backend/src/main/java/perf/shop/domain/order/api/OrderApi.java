package perf.shop.domain.order.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.order.application.OrderFacade;
import perf.shop.domain.order.dto.request.OrderRequest;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderFacade orderFacade;

    @PostMapping("/async")
    public ApiResponse<Void> processOrderAsync(@RequestBody @Valid OrderRequest orderRequest,
                                               @UserId Long userId) {
        orderFacade.processOrderAsync(userId, orderRequest);
        return ApiResponse.of(ResponseCode.ORDER_SUCCESS);
    }

    @PostMapping("/sync")
    public ApiResponse<Void> processOrderSync(@RequestBody @Valid OrderRequest orderRequest,
                                              @UserId Long userId) {
        orderFacade.processOrderSync(userId, orderRequest);
        return ApiResponse.of(ResponseCode.ORDER_SUCCESS);
    }

    @PostMapping("/only-payment")
    public ApiResponse<Void> processOrderWithPaymentApprove(@RequestBody @Valid OrderRequest orderRequest,
                                                            @UserId Long userId) {
        orderFacade.processOrderWithPaymentApprove(userId, orderRequest);
        return ApiResponse.of(ResponseCode.ORDER_SUCCESS);
    }
}
