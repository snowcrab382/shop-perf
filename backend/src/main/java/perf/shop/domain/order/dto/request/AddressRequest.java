package perf.shop.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddressRequest {

    @NotBlank
    private String roadAddress;

    @NotBlank
    private String addressDetail;

    @NotBlank
    private String zipcode;
}
