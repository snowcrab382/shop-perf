package perf.shop.domain.payment.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.domain.payment.application.PaymentService;
import perf.shop.domain.payment.dto.request.PaymentConfirmRequest;
import perf.shop.domain.payment.dto.response.PaymentConfirmResponse;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentApi {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ApiResponse<PaymentConfirmResponse> approvePayment(@RequestBody @Valid PaymentConfirmRequest request) {
        return ApiResponse.of(ResponseCode.PAYMENT_CONFIRMED, paymentService.approvePayment(request));
    }
}
