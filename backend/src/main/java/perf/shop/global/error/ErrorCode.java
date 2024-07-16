package perf.shop.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    TEST_ERROR(404, "테스트 에러입니다.");

    private final int status;
    private final String message;

}
