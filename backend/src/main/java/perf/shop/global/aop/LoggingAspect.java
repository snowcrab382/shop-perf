package perf.shop.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(perf.shop.domain..*)")
    private void cut() {
    }

    @Around("cut()")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {

        // 메서드 시작
//        log.info("시작: {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

        // 실제 메서드 실행
        try {
            Object result = joinPoint.proceed();

            // 메서드 종료
//            log.info("종료: {}.{}()", joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName());
            return result;
        } catch (Exception e) {
            // 메서드 예외
            log.error("예외: {}.{}(), 클래스명 : {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getClass());
            throw e;
        }
    }
}
