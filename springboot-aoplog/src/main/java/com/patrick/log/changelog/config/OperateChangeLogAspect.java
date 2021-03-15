package com.patrick.log.changelog.config;

import com.patrick.log.changelog.annotation.OperateChangeLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.swing.text.html.Option;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author patrick
 * @date 2021/3/15 下午2:08
 * @Des 最簡單的事是堅持，最難的事還是堅持
 */
@Aspect
public class OperateChangeLogAspect {
    /**
     * 拦截带有@OperateChangeLog的方法或带有该注解的类
     */
    @Pointcut("@annotation(com.patrick.log.changelog.annotation.OperateChangeLog) || @within(com.patrick.log.changelog.annotation.OperateChangeLog)")
    private void logPointcut() {
    }

    @Around(value = "logPointcut()")
    private Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        //获取注解相关信息
        OperateChangeLog changeLog = getOperateChangeLog(method);
        Integer value = changeLog.type().getValue();
        //创建

        //更新
        //删除

        long startTime = System.currentTimeMillis();

        //执行方法
        Object result = pjp.proceed();

//        execLogConsumer(level, logMsg);

        return result;
    }

    private OperateChangeLog getOperateChangeLog(Method method) {
        return Optional.ofNullable(method.getAnnotation(OperateChangeLog.class))
                .orElse(method.getDeclaringClass().getAnnotation(OperateChangeLog.class));
    }
}
