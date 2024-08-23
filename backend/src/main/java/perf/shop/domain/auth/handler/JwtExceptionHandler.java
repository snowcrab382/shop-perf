package perf.shop.domain.auth.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import perf.shop.domain.auth.exception.JwtNotFoundException;
import perf.shop.global.error.ErrorResponse;
import perf.shop.global.error.exception.GlobalErrorCode;

@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler(JwtNotFoundException.class)
    protected ErrorResponse handleJwtNotFoundException(JwtNotFoundException e) {
        return ErrorResponse.of(e.getErrorCode());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ErrorResponse handleExpiredJwtException(ExpiredJwtException e) {
        return ErrorResponse.of(GlobalErrorCode.JWT_EXPIRED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected ErrorResponse handleMalformedJwtException(MalformedJwtException e) {
        return ErrorResponse.of(GlobalErrorCode.JWT_MALFORMED);
    }

    @ExceptionHandler(SignatureException.class)
    protected ErrorResponse handleSignatureException(SignatureException e) {
        return ErrorResponse.of(GlobalErrorCode.JWT_SIGNATURE_INVALID);
    }

}
