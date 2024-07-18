package perf.shop.domain.auth.dto;

import static perf.shop.domain.auth.domain.OAuth2Attributes.EMAIL;
import static perf.shop.domain.auth.domain.OAuth2Attributes.NAME;
import static perf.shop.domain.auth.domain.OAuth2Attributes.PROVIDER_ID_NA;
import static perf.shop.domain.auth.domain.OAuth2Attributes.PROVIDER_NA;
import static perf.shop.domain.auth.domain.OAuth2Attributes.RESPONSE;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;


    public NaverResponse(Map<String, Object> attribute) {

        this.attribute = (Map<String, Object>) attribute.get(RESPONSE.getAttribute());
    }

    @Override
    public String getProvider() {

        return PROVIDER_NA.getAttribute();
    }

    @Override
    public String getProviderId() {

        return attribute.get(PROVIDER_ID_NA.getAttribute()).toString();
    }

    @Override
    public String getEmail() {

        return attribute.get(EMAIL.getAttribute()).toString();
    }

    @Override
    public String getName() {

        return attribute.get(NAME.getAttribute()).toString();
    }

}
