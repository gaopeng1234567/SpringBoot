package com.patrick.log.changelog.model;

import lombok.*;

/**
 * @author patrick
 * @date 2021/3/15 上午9:58
 * @Des 属性值变更记录实体
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChangeValueLog {
    private String id;
    private String userId;
    private String userName;
    private String fieldName;
    private Object oldValue;
    private Object newValue;
    private String type;
    private String execTime;

}
