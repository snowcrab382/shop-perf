package perf.shop.global.error;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private List<FieldError> errors;

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
