package com.patrick.log.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author patrick
 * @date 2021/2/27 1:57 下午
 * @Des 测试统一日志拦截
 * 最簡單的事是堅持，最難的事還是堅持
 */
@RestController
public class DemoController {


    @PostMapping("/postParamTest")
    public String postParamTest(@RequestBody Map map) {
        return JSON.toJSONString(map);
    }

    @GetMapping("/getParamTest")
    public String getParamTest(@RequestParam String id) {
        return id;
    }

    @GetMapping("/error")
    public String getError(@RequestParam String id) {
        return 1 / 0 + "";
    }
}
