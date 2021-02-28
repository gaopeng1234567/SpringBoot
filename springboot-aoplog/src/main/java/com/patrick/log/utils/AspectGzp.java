package com.patrick.log.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.SortedMap;

/**
 * @author GP
 * @date 2019/5/21 10:02 AM
 * <p>
 * 拦截日志、记录访问时间。访问参数等
 */
@Slf4j
public class AspectGzp {

    public static void beforeWebLogExec(JoinPoint point, HttpServletRequest request, ThreadLocal<String> reqId) throws IOException {


        StringBuilder logSb = new StringBuilder();
        logSb.append("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        logSb.append("\n# ID           : ").append(reqId.get());
        logSb.append("\n# URL          : ").append(request.getRequestURL().toString());
        logSb.append("\n# HTTP_METHOD  : ").append(request.getMethod());
        logSb.append("\n# IP           : ").append(request.getRemoteAddr());
        logSb.append("\n# CLASS_METHOD : ").append(point.getSignature().getDeclaringTypeName())
                .append(".").append(point.getSignature().getName());
        logSb.append("\n# REQUEST_PARAS: \n").append(JSON.toJSONString(point.getArgs(), true));
        logSb.append("\n# METHOD_ARGS  : ").append(Arrays.toString(point.getArgs()));
        logSb.append("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        log.info(logSb.toString());
    }

    public static void afterWebLogReturning(Object ret, ThreadLocal<Long> startTime, ThreadLocal<String> reqId) {
        //如果ret是不同类型，可以instanceof转换，然后处理
        StringBuilder sb = new StringBuilder();
        sb.append("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        // 处理完请求，返回内容
        sb.append("\n# ID        : ").append(reqId.get());
        sb.append("\n# RESPONSE  : \n").append(JSON.toJSONString(ret));
        sb.append("\n# SPEND_TIME: ").append((System.currentTimeMillis() - startTime.get())).append(" ms");
        sb.append("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        log.info(sb.toString());
    }

    public static void exeException() {

    }
}
