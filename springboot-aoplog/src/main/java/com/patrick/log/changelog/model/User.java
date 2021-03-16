package com.patrick.log.changelog.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author patrick
 * @date 2021/3/15 上午11:11
 * @Des 用户
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Builder
@Setter
@Getter
public class User {
    private String userId;
    private String userName;
}
