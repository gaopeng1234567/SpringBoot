package com.patrick.log.service;

import com.patrick.log.changelog.enums.OperateTaskTypeEnum;
import com.patrick.log.changelog.model.IgnoreFields;
import com.patrick.log.changelog.model.Task;
import com.patrick.log.changelog.model.User;
import org.springframework.scheduling.annotation.Async;

/**
 * @author patrick
 * @date 2021/3/15 上午10:54
 * @Des 操作日志服务类
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class OperateChangeLogServiceImpl implements OperateChangeLogService {

    public static String[] ignoreFields = new String[]{IgnoreFields.UPDATE_TIME, IgnoreFields.ID};



    @Override
    public void asyncLogCreate(Task task) {

    }

    @Override
    public void asyncLogUpload(String log, OperateTaskTypeEnum type) {

    }

    @Override
    public void asyncUpdateLog(String desc, Object mewObj, User oldObj) {

    }

    @Override
    public void asyncDeleteLog(Task task) {

    }
}
