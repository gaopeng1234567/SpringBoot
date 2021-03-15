package com.patrick.log.changelog.enums;

/**
 * @author patrick
 * @date 2021/3/15 上午10:18
 * @Des 操作日志枚举类
 * 最簡單的事是堅持，最難的事還是堅持
 */

public enum OperateTaskTypeEnum {

    CREATE(1, "创建任务"),
    UPDATE(2, "更新属性"),
    DELETE(3, "删除任务"),
    SYS_CREATE_TASK(4, "系统创建任务"),
    SYS_CREATE_FILE(5, "系统创建属性"),
    SYS_UPDATE(6, "系统更新属性"),
    SYS_DELETE(7, "系统删除属性");
//  .
//  .
//  .


    private final Integer value;
    private final String descName;

    OperateTaskTypeEnum(Integer value, String descName) {
        this.value = value;
        this.descName = descName;
    }


    public String getDescName() {
        return descName;
    }

    public Integer getValue() {
        return value;
    }
}
