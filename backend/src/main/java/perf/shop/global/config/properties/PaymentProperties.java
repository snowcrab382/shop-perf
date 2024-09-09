package perf.shop.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {

    private String secretKey;
    private String baseUrl;
    private String confirmEndpoint;
    private String checkEndpoint;

    public String getConfirmUrl() {
        return baseUrl + confirmEndpoint;
    }

    public String getCheckUrl() {
        return baseUrl + checkEndpoint;
    }

}
