package com.patrick.mybatis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author patrick
 * @date 2021/2/25 11:14 下午
 * @Des JDK动态代理类
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class JDKProxy implements InvocationHandler {

    private Object target;//目标对象

    public JDKProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("**********进行增强**********");
        return method.invoke(target, args);
    }

    public static void main(String[] args) {
        JDKProxy jdkProxy = new JDKProxy(new JdkProxyDemoImpl());
        JdkProxyDemo jdkProxyDemo = (JdkProxyDemo) Proxy.newProxyInstance(
                JDKProxy.class.getClassLoader(),
                new Class[]{JdkProxyDemo.class},
                jdkProxy);
        jdkProxyDemo.fun_1();
//        jdkProxyDemo.fun_2();

//        --------聚合代理--------
//        JDKProxy jdkProxy = new JDKProxy(new JdkProxyDemoImpl());
//
//        JdkProxyDemo jdkProxyDemo = (JdkProxyDemo) Proxy.newProxyInstance(
//                JDKProxy.class.getClassLoader(),
//                new Class[]{JdkProxyDemo.class},
//                jdkProxy);


//        LogProxy logProxy = new LogProxy(new JdkProxyDemoImpl());
//        JdkProxyDemo jdkProxyDemo1 = (JdkProxyDemo) Proxy.newProxyInstance(
//                JdkProxyDemo.class.getClassLoader(),
//                new Class[]{JdkProxyDemo.class},
//                logProxy);
//        jdkProxyDemo1.fun_1();

    }
}

class LogProxy implements InvocationHandler {

    private Object target;

    public LogProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("**********进行日志处理**********");
        return method.invoke(target, args);
    }
}
