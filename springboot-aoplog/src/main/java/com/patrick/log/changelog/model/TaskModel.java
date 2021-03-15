package com.patrick.log.changelog.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author patrick
 * @date 2021/3/15 上午10:33
 * @Des 任务模型
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Builder
@Setter
@Getter
public class TaskModel {
    private String id;
    private String name;
    private String desc;
    private String[] tagList;
    private List<Map<String, String>> newDoc;
    private Map<String, String> mapString;
    private Map<String, OtherModel> mapOther;
}
