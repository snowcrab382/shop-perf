package perf.shop.domain.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Attributes {

    PROVIDER_GO("google"),
    PROVIDER_NA("naver"),
    PROVIDER_ID_GO("sub"),
    PROVIDER_ID_NA("id"),
    RESPONSE("response"),
    EMAIL("email"),
    NAME("name"),
    ID("id"),
    USERNAME("username"),
    ROLE("role"),
    AUTHORIZATION("Authorization");

    private final String attribute;

}
