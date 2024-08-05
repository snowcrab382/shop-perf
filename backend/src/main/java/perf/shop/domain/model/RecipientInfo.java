package perf.shop.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class RecipientInfo {

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String streetAddress;

    @Column(length = 100)
    private String detailAddress;

    @Column(length = 50, nullable = false)
    private String zipCode;
}
