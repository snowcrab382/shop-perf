package perf.shop.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInformation {

    private String role;
    private String email;
    private String name;
    private String userName;


    public static UserInformation from(OAuth2Response response) {
        return UserInformation.builder()
                .role("ROLE_USER")
                .email(response.getEmail())
                .name(response.getName())
                .userName(response.getProvider() + "_" + response.getProviderId())
                .build();
    }
}
