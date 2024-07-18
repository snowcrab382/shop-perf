package perf.shop.domain.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Enum {

    GOOGLE("google", "sub", "email", "name"),
    NAVER("naver", "id", "email", "name");

    private final String provider;
    private final String providerId;
    private final String email;
    private final String name;
}
