package perf.shop.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    /**
     * Global Error
     */
    INTERNAL_SERVER_ERROR(500, "서버에서 핸들링되지 않은 에러가 발생하였습니다."),
    METHOD_ARGUMENT_NOT_VALID(400, "요청 값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 메소드입니다."),
    ACCESS_DENIED(401, "접근 권한이 없습니다."),
    JWT_NOT_FOUND(403, "토큰이 존재하지 않습니다."),
    JWT_MALFORMED(403, "토큰이 변조되었습니다."),
    JWT_EXPIRED(403, "토큰이 만료되었습니다."),
    JWT_SIGNATURE_INVALID(403, "토큰의 시그니쳐가 유효하지 않습니다."),
    DATABASE_CONNECTION_TIMEOUT(500, "데이터베이스 연결 시간을 초과하였습니다. 잠시 후 다시 시도해주세요."),
    CIRCUIT_BREAKER_OPEN(500, "서비스가 현재 요청을 처리할 수 없습니다. 잠시 후 다시 시도해주세요."),

    /**
     * EntityNotFoundException
     */
    CATEGORY_NOT_FOUND(400, "카테고리를 찾을 수 없습니다."),
    USER_NOT_FOUND(400, "사용자를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(400, "상품을 찾을 수 없습니다."),
    CART_PRODUCT_NOT_FOUND(400, "장바구니 상품을 찾을 수 없습니다."),
    ADDRESS_BOOK_NOT_FOUND(400, "주소록을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(400, "주문을 찾을 수 없습니다."),
    OUTBOX_NOT_FOUND(400, "아웃박스를 찾을 수 없습니다."),

    /**
     * InvalidValueException
     */
    ORDER_LINE_NOT_EXIST(400, "주문 상품은 최소 1개 이상이어야 합니다."),
    ORDER_ALREADY_EXISTS(400, "이미 주문이 존재합니다."),
    INVALID_ORDER_STATE(400, "주문 상태가 유효하지 않습니다."),
    INVALID_PAYMENT_AMOUNT(400, "결제 금액이 유효하지 않습니다."),

    /**
     * OutOfStockException
     */
    PRODUCT_OUT_OF_STOCK(400, "상품의 재고가 부족합니다."),

    /**
     * RequestTimeoutException
     */
    PAYMENT_REQUEST_TIMEOUT(408, "결제 요청 시간을 초과하였습니다."),

    ;

    private final int status;
    private final String message;

}
