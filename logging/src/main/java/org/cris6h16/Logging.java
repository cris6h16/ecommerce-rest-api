//package org.cris6h16;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//@Aspect
//@Component
//@Slf4j
//public class Logging {
//
//    // Pointcut to log all methods except the Logging class
//    @Before("execution(* org.cris6h16..*(..)) && !execution(* org.cris6h16.Logging.*(..))")
//    public void bef(JoinPoint joinPoint) {
//        StringBuilder argsLog = new StringBuilder();
//        Object[] args = joinPoint.getArgs();
//        for (int i = 0; i < args.length; i++) {
//            argsLog.append("arg[").append(i).append("]: ").append(args[i]).append("; ");
//        }
//
//        log.debug("Method {} called with arguments: [{}]",
//                joinPoint.getSignature(),
//                argsLog.toString());
//    }
//
//    @After("execution(* org.cris6h16..*(..)) && !execution(* org.cris6h16.Logging.*(..))")
//    public void aft(JoinPoint joinPoint) {
//        log.debug("Method {} executed", joinPoint.getSignature());
//    }
//
//    @AfterReturning(pointcut = "execution(* org.cris6h16..*(..)) && !execution(* org.cris6h16.Logging.*(..))", returning = "result")
//    public void aftRet(JoinPoint joinPoint, Object result) {
//        log.debug("Method {} returned: {}",
//                joinPoint.getSignature(),
//                result != null ? result.toString() : "null");
//    }
//
//    @AfterThrowing(pointcut = "execution(* org.cris6h16..*(..)) && !execution(* org.cris6h16.Logging.*(..))", throwing = "exception")
//    public void aftThrow(JoinPoint joinPoint, Throwable exception) {
//        log.error("Method {} threw an exception: {}",
//                joinPoint.getSignature(),
//                exception.getMessage(), exception);
//    }
//
//    @PostConstruct
//    public void init() {
//        log.info("Aspect Logging class initialized.");
//    }
//}
