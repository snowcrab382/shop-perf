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
import org.springframework.web.filter.OncePerRequestFilter;
import perf.shop.domain.user.dto.CustomOAuth2User;
import perf.shop.domain.user.dto.UserInformation;
import perf.shop.global.util.CookieUtil;
import perf.shop.global.util.JwtUtil;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //쿠키가 비어있거나, Authorization 쿠키가 존재하지 않는 경우 반환
        if (!CookieUtil.hasCookie(request, AUTHORIZATION.getAttribute())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = CookieUtil.getCookieValue(request, AUTHORIZATION.getAttribute());

        //토큰 만료기한 검증
        if (JwtUtil.isExpired(token)) {
            log.info(request.getRequestURL() + " : token expired");
            filterChain.doFilter(request, response);

            return;
        }

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

    }

}
