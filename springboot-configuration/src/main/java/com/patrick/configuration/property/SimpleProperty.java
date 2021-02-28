package com.patrick.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "user.info")
public class SimpleProperty {

    private String name;

    private int age;

    private String single;

}
