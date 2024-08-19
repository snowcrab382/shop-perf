package perf.shop.domain.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressRequest {

    @NotBlank
    private String roadAddress;

    @NotBlank
    private String addressDetail;

    @NotBlank
    private String zipcode;

    @Builder
    private AddressRequest(String roadAddress, String addressDetail, String zipcode) {
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }
}
