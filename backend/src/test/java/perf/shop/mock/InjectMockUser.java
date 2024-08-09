package perf.shop.mock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory.class)
public @interface InjectMockUser {

    String username() default "test@naver.com";

    String role() default "ROLE_USER";

    long userId() default 1L;

}
