package perf.shop.domain.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiverRequest {

    @NotBlank
    private String receiverName;

    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
    private String receiverPhone;

    private String requestMessage;

    @Builder
    private ReceiverRequest(String receiverName, String receiverPhone, String requestMessage) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.requestMessage = requestMessage;
    }
}
