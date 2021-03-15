package com.patrick.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author patrick
 * @date 2021/3/12 2:25 下午
 * @Des 模拟任务
 * 最簡單的事是堅持，最難的事還是堅持
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @PostMapping
    public String createTaskMethod() {
        return "";
    }

    @DeleteMapping
    public String deleteTaskMethod() {
        return "";
    }

    @PostMapping
    public String updateTaskMethod() {
        return "";
    }

}
