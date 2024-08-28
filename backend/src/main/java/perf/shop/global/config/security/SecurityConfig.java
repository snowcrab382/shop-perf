package perf.shop.global.config.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;
import perf.shop.domain.auth.filter.JwtFilter;
import perf.shop.domain.auth.handler.OAuth2LoginSuccessHandler;
import perf.shop.domain.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import perf.shop.domain.user.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2LoginSuccessHandler customSuccessHandler;
    private final HandlerExceptionResolver exceptionResolver;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                          OAuth2LoginSuccessHandler customSuccessHandler,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.customSuccessHandler = customSuccessHandler;
        this.exceptionResolver = exceptionResolver;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        RequestMatcher publicEndpoints = new OrRequestMatcher(
                new AntPathRequestMatcher("/", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/login", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/actuator/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/products/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/categories/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/pavicon.ico"),
                new AntPathRequestMatcher("/token", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/sqs/**")
        );

        http
                //csrf disable
                .csrf(AbstractHttpConfigurer::disable)
                //Form 로그인 방식 disable
                .formLogin(AbstractHttpConfigurer::disable)
                //HTTP Basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)
                //oauth2
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                        .successHandler(customSuccessHandler))
                //경로별 인가 작업
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicEndpoints).permitAll()
                        .anyRequest().authenticated())
                //jwt 검증 필터
                .addFilterBefore(new JwtFilter(exceptionResolver, publicEndpoints),
                        UsernamePasswordAuthenticationFilter.class)
                //세션 설정 : STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
