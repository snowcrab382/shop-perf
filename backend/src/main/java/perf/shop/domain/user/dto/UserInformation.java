package perf.shop.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import perf.shop.domain.auth.dto.OAuth2Response;

@Getter
public class UserInformation {

    private String role;
    private String email;
    private String name;
    @Setter
    private Long userId;
    private String username;
    private String provider;

    @Builder
    private UserInformation(String role, String email, String name, String username, Long userId, String provider) {
        this.role = role;
        this.email = email;
        this.name = name;
        this.username = username;
        this.userId = userId;
        this.provider = provider;
    }

    @Builder
    private UserInformation(String role, String username, Long userId) {
        this.role = role;
        this.username = username;
        this.userId = userId;
    }

    public static UserInformation from(OAuth2Response response) {
        return UserInformation.builder()
                .role("ROLE_USER")
                .email(response.getEmail())
                .name(response.getName())
                .username(response.getProvider() + "_" + response.getProviderId())
                .provider(response.getProvider())
                .build();
    }

    public static UserInformation of(String role, String username, Long userId) {
        return UserInformation.builder()
                .role(role)
                .username(username)
                .userId(userId)
                .build();
    }

}
