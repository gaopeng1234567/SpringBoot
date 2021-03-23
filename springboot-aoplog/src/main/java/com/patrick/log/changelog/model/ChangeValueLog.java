package com.patrick.log.changelog.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author patrick
 * @date 2021/3/15 上午9:58
 * @Des 属性值变更记录实体
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Getter
@Setter
@ToString
@Builder
public class ChangeValueLog {
    private String id;
    private String userId;
    private String userName;
    private String fieldName;
    private Object beforeValue;
    private Object afterValue;
    private String type;
    private LocalDateTime execTime;

    /**
     * 自动设置基本信息
     *
     * @param userInfo
     */
    public ChangeValueLog autoSetBaseInfo(User userInfo) {
        LocalDateTime now = LocalDateTime.now();
        this.execTime = now;

        // 设置操作账号相关信息
        if (userInfo == null) {
            this.userId = "fdev_id";
            this.userName = "系统操作";
            return this;
        }
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserName();
        return this;
    }
}
