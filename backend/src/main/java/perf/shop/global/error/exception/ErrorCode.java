package perf.shop.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Global Error
     */
    TEST_ERROR(404, "테스트 에러입니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 메소드입니다."),
    ACCESS_DENIED(401, "접근 권한이 없습니다."),

    /**
     * Business Error
     */
    PRODUCT_NOT_FOUND(400, "상품을 찾을 수 없습니다.");

    private final int status;
    private final String message;

}
