package perf.shop.domain.delivery.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import perf.shop.domain.model.RecipientInfo;
import perf.shop.domain.user.domain.User;
import perf.shop.global.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "recipient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private RecipientInfo recipientInfo;


}
