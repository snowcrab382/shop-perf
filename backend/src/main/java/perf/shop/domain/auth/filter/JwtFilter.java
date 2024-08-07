package perf.shop.domain.auth.filter;

import static org.springframework.util.PatternMatchUtils.simpleMatch;
import static perf.shop.domain.auth.domain.OAuth2Attributes.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import perf.shop.domain.auth.exception.JwtNotFoundException;
import perf.shop.domain.user.dto.CustomOAuth2User;
import perf.shop.domain.user.dto.UserInformation;
import perf.shop.global.error.exception.ErrorCode;
import perf.shop.global.util.CookieUtil;
import perf.shop.global.util.JwtUtil;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver exceptionResolver;
    private final String[] whitelist;

    public JwtFilter(HandlerExceptionResolver exceptionResolver,
                     String[] whitelist) {
        this.exceptionResolver = exceptionResolver;
        this.whitelist = whitelist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //필터 로직을 수행 할 필요 없는 uri인 경우 곧바로 통과
            if (isInWhitelist(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            //쿠키가 비어있거나, Authorization 쿠키가 존재하지 않는 경우 반환
            if (!CookieUtil.hasCookie(request, AUTHORIZATION.getAttribute())) {
                throw new JwtNotFoundException(ErrorCode.JWT_NOT_FOUND);
            }
            String token = CookieUtil.getCookieValue(request, AUTHORIZATION.getAttribute());

            //토큰 만료기한 검증
            JwtUtil.isValid(token);

            //토큰에서 username과 role 획득
            String username = JwtUtil.getUsername(token);
            String role = JwtUtil.getRole(token);

            //회원 정보 객체 생성
            UserInformation userInformation = UserInformation.builder()
                    .username(username)
                    .role(role)
                    .build();

            //UserDetails에 회원 정보 객체 담기
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userInformation);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                    customOAuth2User.getAuthorities());

            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception 발생 : {}", e.getClass());
            exceptionResolver.resolveException(request, response, null, e);
        }

    }

    private boolean isInWhitelist(String requestURI) {
        return simpleMatch(whitelist, requestURI);
    }

}
