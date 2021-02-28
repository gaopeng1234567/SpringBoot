package com.patrick.schedule;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author patrick
 * @date 2021/2/24 5:17 下午
 * @Des 基于ScheduledExecutorService的定时
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class ScheduledExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(
                () -> System.out.println("执行任务A:" + LocalDateTime.now()),//具体执行的任务
                0,//首次执行的延时时间
                3,//任务执行间隔
                TimeUnit.SECONDS);//间隔时间单位
    }
}
