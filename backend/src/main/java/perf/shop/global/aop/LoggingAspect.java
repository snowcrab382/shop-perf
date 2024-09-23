package perf.shop.global.aop;

//@Slf4j
//@Aspect
//@Component
//public class LoggingAspect {
//
//    @Pointcut("within(perf.shop.domain..*)")
//    private void cut() {
//    }
//
//    @Around("cut()")
//    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            Object result = joinPoint.proceed();
//            return result;
//        } catch (Exception e) {
//            log.error("예외: {}.{}(), 클래스명 : {}", joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(), e.getClass());
//            throw e;
//        }
//    }
//}
