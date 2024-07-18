package perf.shop.domain.auth.dto;

import static perf.shop.domain.auth.domain.OAuth2Attributes.EMAIL;
import static perf.shop.domain.auth.domain.OAuth2Attributes.NAME;
import static perf.shop.domain.auth.domain.OAuth2Attributes.PROVIDER_GO;
import static perf.shop.domain.auth.domain.OAuth2Attributes.PROVIDER_ID_GO;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attribute;


    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return PROVIDER_GO.getAttribute();
    }

    @Override
    public String getProviderId() {

        return attribute.get(PROVIDER_ID_GO.getAttribute()).toString();
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
