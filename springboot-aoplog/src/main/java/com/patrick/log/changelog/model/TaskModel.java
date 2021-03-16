package com.patrick.log.changelog.model;

import com.patrick.log.changelog.annotation.FiledTransConvert;
import com.patrick.log.changelog.enums.OperateTaskTypeEnum;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * @author patrick
 * @date 2021/3/15 上午10:33
 * @Des 任务模型
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Setter
@Getter
public class TaskModel {
    @FiledTransConvert(rename = "测试枚举值",enumConverter = OperateTaskTypeEnum.class)
    private String id;
    @FiledTransConvert(rename = "任务名称")
    private String name;
    @FiledTransConvert(rename = "任务描述")
    private String desc;
    private String[] tagList;
    private List<Map<String, String>> newDoc;
    private Map<String, String> mapString;
    private Map<String, OtherModel> mapOther;

    public TaskModel() {

    }
}
