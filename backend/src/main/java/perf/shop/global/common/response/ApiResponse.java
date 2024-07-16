package perf.shop.global.common.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ofNoContent(ResponseCode responseCode) {
        return ApiResponse.<T>builder()
                .status(responseCode.getStatus())
                .message(responseCode.getMessage())
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> of(ResponseCode responseCode, T data) {
        return ApiResponse.<T>builder()
                .status(responseCode.getStatus())
                .message(responseCode.getMessage())
                .data(data)
                .build();
    }

}
