package perf.shop;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import perf.shop.global.common.response.ApiResponse;
import perf.shop.global.common.response.ResponseCode;
import perf.shop.global.util.CookieUtil;
import perf.shop.global.util.JwtUtil;

@RestController
@RequiredArgsConstructor
public class HelloController {

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
}
