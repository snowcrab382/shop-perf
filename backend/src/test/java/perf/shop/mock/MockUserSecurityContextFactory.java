package perf.shop.mock;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import perf.shop.domain.user.dto.CustomOAuth2User;
import perf.shop.domain.user.dto.UserInformation;

public class MockUserSecurityContextFactory implements
        WithSecurityContextFactory<InjectMockUser> {

    @Override
    public SecurityContext createSecurityContext(InjectMockUser annotation) {
        String role = annotation.role();
        String username = annotation.username();
        Long userId = annotation.userId();

        UserInformation userInformation = UserInformation.of(role, username, userId);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userInformation);

        Authentication auth = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                customOAuth2User.getAuthorities());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);

        return context;
    }
}
