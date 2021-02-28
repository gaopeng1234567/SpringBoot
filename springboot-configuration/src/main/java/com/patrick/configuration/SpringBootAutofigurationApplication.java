package com.patrick.configuration;

import com.patrick.configuration.property.SimpleProperty;
import com.patrick.configuration.random.RandomProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.SimpleFormatter;

/**
 * @author patrick
 * @date 2021/2/27 2:52 下午
 * @Des 配置
 * 最簡單的事是堅持，最難的事還是堅持
 */
@SpringBootApplication
@RestController
public class SpringBootAutofigurationApplication {

    @Autowired
    private RandomProperty randomProperty;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAutofigurationApplication.class, args);
    }

    @GetMapping("/getUserInfo")
    public String getUserInfo() {
        return randomProperty.toString();
    }
}
