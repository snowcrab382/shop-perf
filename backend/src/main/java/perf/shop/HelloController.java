package perf.shop;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.order.repository.OrdersRepository;
import perf.shop.domain.payment.repository.PaymentRepository;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.util.CookieUtil;
import perf.shop.global.util.JwtUtil;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping("/")
    public String healthCheck() {
        return "";
    }

    @PostMapping("/token")
    public ApiResponse<Void> token(HttpServletResponse response) {
        String jwt = JwtUtil.createJwt(1L, "test", "ROLE_USER", 86400000L);
        CookieUtil.addCookie(response, "Authorization", jwt, 86400);
        return ApiResponse.of(ResponseCode.CREATED);
    }

    @GetMapping("/admin/order-count")
    public ApiResponse<Long> getOrderCounts() {
        return ApiResponse.of(ResponseCode.GET, ordersRepository.count());
    }

    @GetMapping("/admin/payment-count")
    public ApiResponse<Long> getPaymentCounts() {
        return ApiResponse.of(ResponseCode.GET, paymentRepository.count());
    }

    @GetMapping("/admin/difference")
    public ApiResponse<List<Map<String, Object>>> getDifferenceBetweenOrderCreatedTimeAndPaymentCreatedTime() {
        return ApiResponse.of(ResponseCode.GET, ordersRepository.findOrderAndPaymentTimeDifference());
    }

    @DeleteMapping("/admin/orders/clear")
    public ApiResponse<Void> deleteAll() {
        ordersRepository.deleteAllInBatch();
        return ApiResponse.of(ResponseCode.DELETED);
    }

    @DeleteMapping("/admin/payments/clear")
    public ApiResponse<Void> deleteAllPayments() {
        paymentRepository.deleteAllInBatch();
        return ApiResponse.of(ResponseCode.DELETED);
    }


}
