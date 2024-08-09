package perf.shop.domain.auth.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static perf.shop.domain.auth.domain.OAuth2Attributes.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.HandlerExceptionResolver;
import perf.shop.domain.auth.exception.JwtNotFoundException;
import perf.shop.global.util.CookieUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] JwtFilter")
class JwtFilterTest {

    JwtFilter jwtFilter;

    @Mock
    HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain chain;

    @BeforeEach
    void setUp() {
        String[] whitelist = new String[]{"/login"};
        jwtFilter = new JwtFilter(handlerExceptionResolver, whitelist);
    }

    @Test
    @DisplayName("화이트리스트에 포함된 uri인 경우 필터 로직을 수행하지 않고 바로 통과")
    void doFilterInternal_Pass_IfRequestUriIsInWhitelist() throws Exception {
        //given
        given(request.getRequestURI()).willReturn("/login");

        //when
        jwtFilter.doFilterInternal(request, response, chain);

        //then
        then(chain).should().doFilter(request, response);
    }

    @Test
    @DisplayName("쿠키가 비어있거나, Authorization이 존재하지 않으면 예외 발생")
    void doFilterInternal_ThrowsException_IfCookieArrayOrAuthorizationCookieIsNull() throws Exception {
        //given
        given(request.getRequestURI()).willReturn("/need-auth");
        given(CookieUtil.getCookie(request, AUTHORIZATION.getAttribute())).willReturn(null);

        //when
        jwtFilter.doFilterInternal(request, response, chain);

        //then
        then(handlerExceptionResolver).should()
                .resolveException(eq(request), eq(response), isNull(), any(JwtNotFoundException.class));
    }

}