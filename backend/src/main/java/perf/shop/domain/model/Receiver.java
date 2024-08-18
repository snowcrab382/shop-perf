package perf.shop.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Receiver {

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String receiverPhone;

    private String requestMessage;

    @Builder
    private Receiver(String receiverName, String receiverPhone, String requestMessage) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.requestMessage = requestMessage;
    }

    public static Receiver of(String receiverName, String receiverPhone, String requestMessage) {
        return Receiver.builder()
                .receiverName(receiverName)
                .receiverPhone(receiverPhone)
                .requestMessage(requestMessage)
                .build();
    }
}
