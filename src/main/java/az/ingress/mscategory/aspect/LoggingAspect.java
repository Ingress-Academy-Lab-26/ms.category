package az.ingress.mscategory.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class LoggingAspect {

    @Before("execution(* az.ingress.*.*.*.*(..))")
    public void logBeforeServiceMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        Object[] args = joinPoint.getArgs();
        log.info("Method \"{}\" is called with this class \"{}\"  with parameters: \"{}\"", methodName, className, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "execution(* az.ingress.*.*.*.*(..))", returning = "result")
    public void logAfterServiceMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        log.info(" This \"{}\"  Method \"{}\" returned: \"{}\" ", className, methodName, result);
    }

    @AfterThrowing(pointcut = "execution(* az.ingress.*.*.*.*(..))", throwing = "ex")
    public void logServiceException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Exception occurred in service method \"{}\": \"{}\"", methodName, ex.getMessage());
    }
}
