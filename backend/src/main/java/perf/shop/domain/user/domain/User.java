package perf.shop.domain.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.user.dto.UserInformation;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
    private String provider;

    @Builder
    public User(String username, String name, String email, String role, String provider) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
    }

    public static User create(UserInformation userInformation) {
        return User.builder()
                .username(userInformation.getUsername())
                .name(userInformation.getName())
                .email(userInformation.getEmail())
                .role(userInformation.getRole())
                .provider(userInformation.getProvider())
                .build();
    }

    public void update(UserInformation userInformation) {
        this.name = userInformation.getName();
        this.email = userInformation.getEmail();
        this.role = userInformation.getRole();
        this.provider = userInformation.getProvider();
    }


}
