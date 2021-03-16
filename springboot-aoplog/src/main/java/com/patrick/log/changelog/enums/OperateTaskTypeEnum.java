package com.patrick.log.changelog.enums;

import com.patrick.log.changelog.annotation.BaseEnum;

/**
 * @author patrick
 * @date 2021/3/15 上午10:18
 * @Des 操作日志枚举类
 * 最簡單的事是堅持，最難的事還是堅持
 */

public enum OperateTaskTypeEnum implements BaseEnum {

    CREATE(1, "创建任务"),
    UPDATE(2, "更新属性"),
    DELETE(3, "删除任务"),
    SYS_CREATE_TASK(4, "系统创建任务"),
    SYS_CREATE_FILE(5, "系统创建属性"),
    SYS_UPDATE(6, "系统更新属性"),
    SYS_DELETE(7, "系统删除属性"),
    UPLOAD_FILE(8, "上传文件");
//  .
//  .
//  .


    /**
     * 枚举值
     */
    private final Integer value;
    /**
     * 操作类型
     */
    private final String opName;

    OperateTaskTypeEnum(Integer value, String opName) {
        this.value = value;
        this.opName = opName;
    }


    public String getOpName() {
        return opName;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return opName;
    }
}
