package perf.shop.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    GET(200, "조회 성공"),
    CREATED(201, "생성 성공"),
    UPDATED(200, "수정 성공"),
    DELETED(200, "삭제 성공"),
    ORDER_COMPLETE(200, "주문 성공"),
    ;

    private final int status;
    private final String message;

}
