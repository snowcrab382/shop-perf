package perf.shop.domain.auth.filter;

import static perf.shop.domain.auth.domain.OAuth2Attributes.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import perf.shop.domain.auth.exception.JwtNotFoundException;
import perf.shop.domain.user.dto.CustomOAuth2User;
import perf.shop.domain.user.dto.UserInformation;
import perf.shop.global.error.exception.GlobalErrorCode;
import perf.shop.global.util.CookieUtil;
import perf.shop.global.util.JwtUtil;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver exceptionResolver;
    private final RequestMatcher publicEndpoints;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //필터 로직을 수행 할 필요 없는 uri 인 경우 곧바로 통과
            if (publicEndpoints.matches(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            //쿠키가 비어있거나, Authorization 쿠키가 존재하지 않는 경우 반환
            if (!CookieUtil.hasCookie(request, AUTHORIZATION.getAttribute())) {
                throw new JwtNotFoundException(GlobalErrorCode.JWT_NOT_FOUND);
            }
            String token = CookieUtil.getCookieValue(request, AUTHORIZATION.getAttribute());

            //토큰 만료기한 검증
            JwtUtil.isValid(token);

            //토큰에서 role, username, userId 획득
            String role = JwtUtil.getRole(token);
            String username = JwtUtil.getUsername(token);
            Long userId = JwtUtil.getUserId(token);

            //회원 정보 객체 생성
            UserInformation userInformation = UserInformation.of(role, username, userId);

            //UserDetails 에 회원 정보 객체 담기
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userInformation);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                    customOAuth2User.getAuthorities());

            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            log.error("JWT 검증 중 예외 발생 : {}", e.getClass());
            exceptionResolver.resolveException(request, response, null, e);
        }

        filterChain.doFilter(request, response);
    }

}
