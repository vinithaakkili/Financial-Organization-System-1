package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.demo.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Entering method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning("execution(* com.example.demo.service.*.*(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        System.out.println("Successfully executed method: " + joinPoint.getSignature().getName());
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.demo.service.*.*(..))",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        System.out.println("Exception in method: "
                + joinPoint.getSignature().getName()
                + " | Error: "
                + exception.getMessage());
    }
}