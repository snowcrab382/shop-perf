package perf.shop.global.util;

import static perf.shop.domain.auth.domain.OAuth2Attributes.ID;
import static perf.shop.domain.auth.domain.OAuth2Attributes.ROLE;
import static perf.shop.domain.auth.domain.OAuth2Attributes.USERNAME;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public static Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(ID.getAttribute(), Long.class);
    }

    public static String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(USERNAME.getAttribute(), String.class);
    }

    public static String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(ROLE.getAttribute(), String.class);
    }

    public static String createJwt(Long userId, String username, String role, Long expirationTime) {
        return Jwts.builder()
                .claim(ID.getAttribute(), userId)
                .claim(USERNAME.getAttribute(), username)
                .claim(ROLE.getAttribute(), role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public static void isValid(String token) {
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }
}
