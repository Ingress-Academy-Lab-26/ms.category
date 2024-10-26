package az.ingress.mscategory.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(az.ingress.mscategory.annotation.Log) || @within(az.ingress.mscategory.annotation.Log)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        log.info("Entering method: {} in class: {} with arguments: {}", methodName, className, Arrays.toString(args));
        Object result;
        try {
            result = joinPoint.proceed(); // Proceed with method execution
            log.info("Method: {} in class: {} returned: {}", methodName, className, result);
        } catch (Exception ex) {
            log.error("Exception in method: {} in class: {} with message: {}", methodName, className, ex.getMessage());
            throw ex;
        }
        return result;
    }
}
