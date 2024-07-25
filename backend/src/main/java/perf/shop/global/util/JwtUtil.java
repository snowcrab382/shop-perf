package perf.shop.global.util;

import static perf.shop.domain.auth.domain.OAuth2Attributes.ROLE;
import static perf.shop.domain.auth.domain.OAuth2Attributes.USERNAME;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {

    private static SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public static String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(USERNAME.getAttribute(), String.class);
    }

    public static String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(ROLE.getAttribute(), String.class);
    }

    public static Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .getExpiration().before(new Date());
    }

    public static String createJwt(String username, String role, Long expirationTime) {
        return Jwts.builder()
                .claim(USERNAME.getAttribute(), username)
                .claim(ROLE.getAttribute(), role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }
}
