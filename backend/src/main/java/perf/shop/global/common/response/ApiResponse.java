package perf.shop.global.common.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    /**
     * 성공적인 응답이지만, 반환할 데이터가 없는 경우 //TODO : data를 null로 반환하는 것에 대해 생각해 봐야 함
     *
     * @param responseCode 응답 성공 시 상태 코드 및 메세지 enum
     * @param <T>          반환 데이터가 없는 경우 일반적으로 Void 타입 반환
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> of(ResponseCode responseCode) {
        return ApiResponse.<T>builder()
                .status(responseCode.getStatus())
                .message(responseCode.getMessage())
                .data(null)
                .build();
    }

    /**
     * 성공적인 응답이며, 반환할 데이터가 있는 경우
     *
     * @param responseCode 응답 성공 시 상태 코드 및 메세지 enum
     * @param data         반환할 데이터
     * @param <T>          반환할 데이터의 타입
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> of(ResponseCode responseCode, T data) {
        return ApiResponse.<T>builder()
                .status(responseCode.getStatus())
                .message(responseCode.getMessage())
                .data(data)
                .build();
    }

}
