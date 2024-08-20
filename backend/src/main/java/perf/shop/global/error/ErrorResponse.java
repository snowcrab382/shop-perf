package perf.shop.global.error;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import perf.shop.global.error.exception.ErrorCode;

@Getter
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private List<FieldError> errors;

    /**
     * 일반적인 예외 처리 error 에 대한 내용이 없는 경우 사용. errors 필드에는 null 이 아닌 빈 리스트를 반환
     *
     * @return ErrorResponse
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .errors(Collections.emptyList())
                .build();
    }

    /**
     * MethodArgumentNotValidException 예외 처리
     *
     * @return ErrorResponse
     */
    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> errors) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();
    }

    @Getter
    @Builder
    public static class FieldError {

        private String field;
        private Object value;
        private String message;

        /**
         * BindingResult 의 FieldError 정보를 커스텀 FieldError 로 변환
         *
         * @param bindingResult MethodArgumentNotValidException 에서 전달받은 BindingResult
         * @return List<FieldError>
         */
        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> FieldError.builder()
                            .field(fieldError.getField())
                            .value(fieldError.getRejectedValue())
                            .message(fieldError.getDefaultMessage())
                            .build())
                    .collect(Collectors.toList());
        }
    }

}
