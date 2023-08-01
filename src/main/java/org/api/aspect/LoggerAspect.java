package org.api.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggerAspect.class);

    @Before("within(@org.api.annotation.LogExecutionTime *)")
    public void logExecutionTimeBefore(JoinPoint joinPoint) throws Throwable {
        log.info("### " + joinPoint.getSignature().getName() + "() START ###");
    }

    @AfterReturning("within(@org.api.annotation.LogExecutionTime *)")
    public void logExecutionTimeAfter(JoinPoint joinPoint) throws Throwable {
        log.info("### " + joinPoint.getSignature().getName() + "() END ###");
    }

}