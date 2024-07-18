package perf.shop.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInformation {

    private String role;
    private String email;
    private String name;
    private String username;


    @Builder
    public UserInformation(String role, String email, String name, String username) {
        this.role = role;
        this.email = email;
        this.name = name;
        this.username = username;
    }

    @Builder
    public UserInformation(String role, String username) {
        this.role = role;
        this.username = username;
    }

}
