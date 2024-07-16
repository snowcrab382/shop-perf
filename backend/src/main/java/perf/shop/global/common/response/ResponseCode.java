package perf.shop.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    GET(200, "조회 성공"),
    CREATED(201, "조회 성공. 생성됨"),
    ;

    private final int status;
    private final String message;

}
