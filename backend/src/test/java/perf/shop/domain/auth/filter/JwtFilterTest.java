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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;
import perf.shop.domain.auth.exception.JwtNotFoundException;
import perf.shop.global.util.CookieUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] JwtFilter")
class JwtFilterTest {

    @InjectMocks
    JwtFilter jwtFilter;

    @Mock
    HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    RequestMatcher publicEndpoints;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain chain;

    @Test
    @DisplayName("성공 - 화이트리스트에 포함된 uri 인 경우 필터 로직을 수행하지 않고 바로 통과")
    void doFilterInternal_Pass_IfRequestMatchesWithPublicEndpoints() throws Exception {
        //given
        given(publicEndpoints.matches(request)).willReturn(true);

        //when
        jwtFilter.doFilterInternal(request, response, chain);

        //then
        then(chain).should().doFilter(request, response);
    }

    @Test
    @DisplayName("실패 - 쿠키가 비어있거나, Authorization 이 존재하지 않으면 예외 발생")
    void doFilterInternal_ThrowException_IfCookieArrayOrAuthorizationCookieIsNull() throws Exception {
        //given
        given(publicEndpoints.matches(request)).willReturn(false);
        given(CookieUtil.getCookie(request, AUTHORIZATION.getAttribute())).willReturn(null);

        //when
        jwtFilter.doFilterInternal(request, response, chain);

        //then
        then(handlerExceptionResolver).should()
                .resolveException(eq(request), eq(response), isNull(), any(JwtNotFoundException.class));
    }

}
