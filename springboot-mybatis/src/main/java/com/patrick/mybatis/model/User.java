package com.patrick.mybatis.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}