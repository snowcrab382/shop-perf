package perf.shop.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String roadAddress;

    private String addressDetail;

    private String zipcode;

    @Builder
    private Address(String roadAddress, String addressDetail, String zipcode) {
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }

    public static Address of(String roadAddress, String addressDetail, String zipcode) {
        return Address.builder()
                .roadAddress(roadAddress)
                .addressDetail(addressDetail)
                .zipcode(zipcode)
                .build();
    }
}
