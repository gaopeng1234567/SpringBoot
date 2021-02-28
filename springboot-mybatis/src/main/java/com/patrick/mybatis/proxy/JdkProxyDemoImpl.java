package com.patrick.mybatis.proxy;

/**
 * @author patrick
 * @date 2021/2/25 11:13 下午
 * @Des 最簡單的事是堅持，最難的事還是堅持
 */
public class JdkProxyDemoImpl implements JdkProxyDemo {
    @Override
    public void fun_1() {
        System.out.println("**********我是fun_1**********");
    }

    @Override
    public void fun_2() {
        System.out.println("**********我是fun_2**********");

    }
}
