package perf.shop.domain.order.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.order.domain.Order;
import perf.shop.domain.order.repository.OrdersRepository;
import perf.shop.global.annotation.UserId;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderSearchApi {

    private final OrdersRepository ordersRepository;

    @GetMapping
    public ApiResponse<List<Order>> searchOrders(@UserId Long userId) {
        List<Order> orders = ordersRepository.findAllByUserId(userId);
        return ApiResponse.of(ResponseCode.GET, orders);
    }
}
