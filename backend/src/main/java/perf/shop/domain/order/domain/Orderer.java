package perf.shop.domain.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orderer {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String ordererName;

    @Column(nullable = false)
    private String ordererEmail;

    @Builder
    private Orderer(Long userId, String ordererName, String ordererEmail) {
        this.userId = userId;
        this.ordererName = ordererName;
        this.ordererEmail = ordererEmail;
    }

    public static Orderer of(Long userId, String ordererName, String ordererEmail) {
        return Orderer.builder()
                .userId(userId)
                .ordererName(ordererName)
                .ordererEmail(ordererEmail)
                .build();
    }
}
