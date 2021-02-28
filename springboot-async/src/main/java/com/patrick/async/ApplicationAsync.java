package com.patrick.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author patrick
 * @date 2021/2/24 4:07 下午
 * @Des 启动类
 * 最簡單的事是堅持，最難的事還是堅持
 */

@SpringBootApplication
@EnableAsync
public class ApplicationAsync {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationAsync.class, args);
    }
}
