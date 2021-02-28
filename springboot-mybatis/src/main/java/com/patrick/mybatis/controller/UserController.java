package com.patrick.mybatis.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.patrick.mybatis.model.User;
import com.patrick.mybatis.transaction.TransactionServiceDemo;
import com.patrick.mybatis.service.UserService1;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private UserService1 userService;

    @Resource
    private TransactionServiceDemo transactionServiceDemo;

    @GetMapping("/getUserList")
    public String getUserList() {
        return
                JSON.toJSONString(userService.findAllUser());
    }

    @GetMapping("/getUserListPage")
    public String getUserListPage() {
        Page<User> userPage = new Page<>();
        userPage.setCurrent(1);
        userPage.setSize(2);
        return
                JSON.toJSONString(userService.selectUserPage(userPage));
    }

    @PostMapping("/insertUser")
    public void insertUser() throws Exception {
        transactionServiceDemo.tranFather_v3();
    }

}