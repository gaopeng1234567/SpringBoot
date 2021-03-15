package com.patrick.log.controller;

import com.patrick.log.changelog.model.TaskModel;
import com.patrick.log.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author patrick
 * @date 2021/3/15 上午10:50
 * @Des 任务接口
 * 最簡單的事是堅持，最難的事還是堅持
 */
@RestController
@RequestMapping("/api/task/")
public class TaskController {

    private TaskService taskService;

    public void createTask(TaskModel taskModel) {

    }
}
