package perf.shop.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

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

    /**
     * Business Error
     */
    CATEGORY_NOT_FOUND(400, "카테고리를 찾을 수 없습니다."),
    USER_NOT_FOUND(400, "사용자를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(400, "상품을 찾을 수 없습니다."),
    CART_PRODUCT_NOT_FOUND(400, "장바구니 상품을 찾을 수 없습니다."),
    ;

    private final int status;
    private final String message;

}
