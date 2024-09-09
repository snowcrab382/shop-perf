package perf.shop.domain.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    EASY_PAY("간편결제"),
    MOBILE("휴대폰"),
    BANK_TRANSFER("계좌이체"),
    CULTURE_VOUCHER("문화상품권"),
    BOOK_CULTURE_VOUCHER("도서문화상품권"),
    GAME_CULTURE_VOUCHER("게임문화상품권"),
    NORMAL("일반"),
    ;

    private final String value;
}
