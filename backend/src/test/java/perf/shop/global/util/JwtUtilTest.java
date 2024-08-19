package perf.shop.global.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("[단위 테스트] JwtUtil")
class JwtUtilTest {

    @BeforeEach
    void setUp() {
        String secretKey = "testsecretkeytestsecretkeytestsecretkeytestsecretkeytestsecretkeytestsecretkey";
        ReflectionTestUtils.setField(JwtUtil.class, "secretKey",
                Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    @Nested
    @DisplayName("jwt 유효성 검사 테스트")
    class IsValid {

        @Test
        @DisplayName("실패 - jwt가 변조되었다면 예외 발생")
        void isValid_throwException_IfMalformed() {
            // given
            String jwt = JwtUtil.createJwt(1L, "user", "ROLE_USER", 1000L);
            String malformedJwt = "a" + jwt;

            // when & then
            assertThatThrownBy(() -> JwtUtil.isValid(malformedJwt))
                    .isInstanceOf(MalformedJwtException.class);
        }

        @Test
        @DisplayName("실패 - jwt의 시그니쳐가 변경되었다면 예외 발생")
        void isValid_throwException_IfSignatureIsDifferent() {
            // given
            String jwt = JwtUtil.createJwt(1L, "user", "ROLE_USER", 1000L);
            String differentSignatureJwt = jwt + "a";

            // when & then
            assertThatThrownBy(() -> JwtUtil.isValid(differentSignatureJwt))
                    .isInstanceOf(SignatureException.class);
        }

        @Test
        @DisplayName("실패 - jwt가 만료되었다면 예외 발생")
        void isValid_throwException_IfExpired() {
            // given
            String expiredJwt = JwtUtil.createJwt(1L, "user", "ROLE_USER", 0L);

            // when & then
            assertThatThrownBy(() -> JwtUtil.isValid(expiredJwt))
                    .isInstanceOf(ExpiredJwtException.class);
        }

    }

}
