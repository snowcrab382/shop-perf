package perf.shop.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdererRequest {

    @NotBlank
    private String ordererName;

    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "이메일 형식이 올바르지 않습니다.")
    private String ordererEmail;

    @Builder
    private OrdererRequest(String ordererName, String ordererEmail) {
        this.ordererName = ordererName;
        this.ordererEmail = ordererEmail;
    }

}
