package perf.shop.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.user.dto.UserInformation;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 20)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 10, nullable = false)
    private String role;

    @Column(length = 10)
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
