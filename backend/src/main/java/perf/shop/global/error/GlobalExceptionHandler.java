package perf.shop.global.error;


import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import perf.shop.global.error.ErrorResponse.FieldError;
import perf.shop.global.error.exception.BusinessException;
import perf.shop.global.error.exception.ErrorCode;
import perf.shop.global.error.exception.GlobalErrorCode;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 유효성 검사에 통과하지 못한 경우 발생하는 예외 처리
     *
     * @param e valid 애노테이션으로 인해 binding error 발생 시 예외 발생
     * @return ErrorResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = FieldError.of(e.getBindingResult());
        return ErrorResponse.of(GlobalErrorCode.METHOD_ARGUMENT_NOT_VALID, fieldErrors);
    }

    /**
     * 허용되지 않은 HTTP 메서드 요청에 대한 예외 처리
     *
     * @param e @XXXMapping 에 포함되지 않은 HTTP 메서드 요청이 들어왔을 때 예외 발생
     * @return ErrorResponse
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ErrorResponse.of(GlobalErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * Authorization 객체가 필요한 권한을 보유하지 않은 경우에 대한 예외 처리
     *
     * @param e 접근 권한이 없는 경우 예외 발생
     * @return ErrorResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return ErrorResponse.of(GlobalErrorCode.ACCESS_DENIED);
    }

    /**
     * 비즈니스 로직에서 발생하는 예외 처리
     *
     * @param e 비즈니스 로직 중 부적절한 값이나 존재하지 않는 엔티티를 찾았을 때 예외 발생
     * @return ErrorResponse
     */
    @ExceptionHandler(BusinessException.class)
    protected ErrorResponse handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ErrorResponse.of(errorCode);
    }

    /**
     * 직접 핸들링하지 않은 예외들을 처리
     *
     * @param e 핸들링하지 않은 예외들(ex. InvalidDataAccessApiUsageException)
     * @return ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        return ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
