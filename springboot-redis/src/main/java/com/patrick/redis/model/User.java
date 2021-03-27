package com.patrick.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author patrick
 * @date 2021/3/26 10:49 上午
 * @Des 用户
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 年龄
     */
    private Integer age;
    /**
     * 爱好
     */
    private String like;

}
