package perf.shop.global.error;


import java.util.List;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import perf.shop.global.error.ErrorResponse.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = FieldError.of(e.getBindingResult());
        return ErrorResponse.of(ErrorCode.TEST_ERROR, fieldErrors);
    }
}
