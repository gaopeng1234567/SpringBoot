package com.patrick.log.service;

import com.patrick.log.changelog.enums.OperateTaskTypeEnum;
import com.patrick.log.changelog.model.User;

/**
 * @author patrick
 * @date 2021/3/15 上午10:54
 * @Des 操作日志服务类
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class OperateChangeLogServiceImpl implements OperateChangeLogService {

    @Override
    public void asyncLog(String log, OperateTaskTypeEnum type) {

    }

    @Override
    public void asyncLog(String log, OperateTaskTypeEnum type, User user) {

    }

    @Override
    public void asyncUpdateLog(String desc, Object mewObj, User oldObj) {

    }

    @Override
    public void asyncDeleteLog(String desc, Object obj) {

    }
}
