package com.patrick.configuration.random;

import com.patrick.configuration.property.SimpleProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RandomProperty {

    @Value("${my.secret}")
    private String randomValue;

    @Value("${my.number}")
    private int randomInt;

    @Value("${my.bignumber}")
    private long randomLong;

    @Value("${my.uuid}")
    private String randomUUID;

    @Value("${my.number.less.than.ten}")
    private int randomIntRange;

    @Value("${my.number.in.range}")
    private int randomIntMaxMinRange;

    @Autowired
    private SimpleProperty simpleProperty;

}