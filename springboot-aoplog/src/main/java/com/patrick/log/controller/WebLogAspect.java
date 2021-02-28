package com.patrick.log.controller;

import com.patrick.log.utils.AspectGzp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * @author GP
 * @date 2019/12/9 9:53 AM
 */

@Aspect
@Component
@Slf4j
@Order(-100)
public class WebLogAspect {
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final ThreadLocal<String> reqId = new ThreadLocal<>();


    @Pointcut("execution(public * com.patrick.log.controller.*Controller.*(..))")
    private void pointCut() {
    }

    @Before("pointCut()")
    private void beforeExe(JoinPoint joinPoint) throws IOException {
        startTime.set(System.currentTimeMillis());
        reqId.set(UUID.randomUUID().toString());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
//        HttpServletRequest copyRequest = (HttpServletRequest) new CopyHttpServletRequestWrapper(request);
        AspectGzp.beforeWebLogExec(joinPoint, request, reqId);
    }

    @AfterReturning(returning = "ret", pointcut = "pointCut()")
    public void afterReturning(Object ret) {
        AspectGzp.afterWebLogReturning(ret, startTime, reqId);
        reqId.remove();
        startTime.remove();
    }
}
