package com.patrick.log.service;

import com.patrick.log.changelog.enums.OperateTaskTypeEnum;
import com.patrick.log.changelog.model.User;

/**
 * @author patrick
 * @date 2021/3/15 上午11:10
 * @Des 操作日志
 * 最簡單的事是堅持，最難的事還是堅持
 */
public interface OperateChangeLogService {
    /**
     * 异步记录日志
     *
     * @param log  日志信息
     * @param type 日志类型
     */
    void asyncLog(String log, OperateTaskTypeEnum type);

    void asyncLog(String log, OperateTaskTypeEnum type, User user);

    /**
     * 异步记录数据修改的日志
     *
     * @param desc   描述
     * @param mewObj 新对象
     * @param oldObj 旧对象
     */
    void asyncUpdateLog(String desc, Object mewObj, User oldObj);

    /**
     * 异步记录删除对象日志
     *
     * @param desc 描述
     * @param obj  对象
     */
    void asyncDeleteLog(String desc, Object obj);
}
