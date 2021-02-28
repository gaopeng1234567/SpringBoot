package com.patrick.schedule;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @author patrick
 * @date 2021/2/24 4:27 下午
 * @Des 基于spring Scheduled的定时任务
 * 最簡單的事是堅持，最難的事還是堅持
 *
 * corn 规则 在线https://www.bejson.com/othertools/cron/
 * 第一位，表示秒，取值0-59
 * 第二位，表示分，取值0-59
 * 第三位，表示小时，取值0-23
 * 第四位，日期天/日，取值1-31
 * 第五位，日期月份，取值1-12
 * 第六位，星期，取值1-7，1表示星期天，2表示星期一
 * 第七位，年份，可以留空，取值1970-2099
 *
 * <p>
 * 正常来说，定时任务的时间间隔都是大约业务代码执行的时间的
 * <p>
 * 如果业务代码执行的时间大于定时配置的时间？
 * fixedRate 业务代码执行完毕后立即执行
 * corn、fixedDelay 直到业务代码执行完毕，按照配置的时间开始新的一轮任务
 */

@Component
public class ScheduledTaskDemo {

    @Async
    @Scheduled(cron = "0/1 * * * * ?") //每10秒执行一次
    public void scheduledTaskByCorn() {
        System.out.println(("ScheduledTask Start ByCorn：" + LocalTime.now()));
        doSomeThings();
        System.out.println(("ScheduledTask Stop ByCorn：" + LocalTime.now()));
    }

    @Scheduled(fixedRate = 6000) //每1秒执行一次
    public void scheduledTaskByFixedRate() {
        System.out.println(("ScheduledTask Start ByFixedRate：" + LocalTime.now()));
        doSomeThings();
        System.out.println(("ScheduledTask Stop ByFixedRate：" + LocalTime.now()));
    }

    @Scheduled(fixedDelay = 3000) //每1秒执行一次
    public void scheduledTaskByFixedDelay() {
        System.out.println(("ScheduledTask Start ByFixedDelay：" + LocalTime.now()));
        doSomeThings();
        System.out.println(("ScheduledTask Stop ByFixedDelay：" + LocalTime.now()));
    }

    private void doSomeThings() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}