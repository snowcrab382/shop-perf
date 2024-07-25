package perf.shop.domain.auth.filter;

import static org.springframework.util.PatternMatchUtils.simpleMatch;
import static perf.shop.domain.auth.domain.OAuth2Attributes.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import perf.shop.domain.user.dto.CustomOAuth2User;
import perf.shop.domain.user.dto.UserInformation;
import perf.shop.global.util.JwtUtil;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String[] whitelist = {
            "/",
            "/actuator/**",
            "/login"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (isInWhiteList(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            log.info(request.getRequestURL() + " : has no cookies");
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {

            log.info("cookie name : " + cookie.getName());
            if (cookie.getName().equals(AUTHORIZATION.getAttribute())) {
                authorization = cookie.getValue();
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {

            log.info(request.getRequestURL() + " : has no token");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

        //토큰 소멸 시간 검증
        if (JwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String username = JwtUtil.getUsername(token);
        String role = JwtUtil.getRole(token);

        //userDTO를 생성하여 값 set
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

    }

    private boolean isInWhiteList(String requestUri) {
        return simpleMatch(whitelist, requestUri);
    }
}
