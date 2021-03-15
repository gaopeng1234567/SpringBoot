package com.patrick.mybatis.transaction;

import com.patrick.mybatis.model.User;
import com.patrick.mybatis.service.UserService1;
import com.patrick.mybatis.service.UserService2;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * @author patrick
 * @date 2021/2/24 11:30 下午
 * @Des 事物
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Service
//@Transactional() 如果放在类上，会对当前类下的所有public方法进行事物管理
public class TransactionServiceDemo {

    @Resource
    private UserService1 userService;

    @Resource
    private UserService2 userService2;


    @Transactional()
    public void tranFather_v0() {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());
    }

    //回滚 rollbackFor = {RuntimeException.class} 默认
    @Transactional()
    public void tranFather_v1() throws IOException {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());
        throw new IOException();
    }

    //不回滚
    @Transactional(noRollbackFor = {RuntimeException.class})
    public void tranFather_v2() {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());
        throw new RuntimeException();
    }

    @Transactional(rollbackFor = {MyException.class})
    public void tranFather_v3() throws YourException {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());
        throw new YourException();
    }

    //需求:  父方法调用子方法，子方法报错后进行回滚，父方法不回滚。
    //思考: 如果去了tranFather_v4上面的Transactional 结果是啥？
    @Transactional()
    public void tranFather_v4() {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());
        tranChild();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void tranChild() {
        userService.insertUser(User
                .builder()
                .name("儿子")
                .build());
        int a = 1 / 0;
    }

    /**
     * 第一种方式解决
     */
    @Transactional
    public void tranFather_v5() {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());
        try {
            TransactionServiceDemo TransactionServiceDemo = (TransactionServiceDemo) AopContext.currentProxy();
            TransactionServiceDemo.tranChild();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    /**
     * 第二种方式解决 不同service，开启新的事物
     */
    @Transactional
    public void tranFather_v6() {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());

        try {
            userService2.insertUserException(User
                    .builder()
                    .name("儿子")
                    .build());
        } catch (Exception e) {

        }
    }

    /**
     * propagation = Propagation.NESTED验证
     * NESTED 和 NEW 区别
     * REQUIRES_NEW是通过开启新的事务实现的，内部事务和外围事务是两个事务
     * NESTED是嵌套事务，所以外围方法回滚之后，作为外围方法事务的子事务也会被回滚
     * NESTED和REQUIRES_NEW都可以做到内部方法事务回滚而不影响外围方法事务
     * --------------------------------------------------------------------------------------------------
     * NESTED 和默认 REQUIRED 区别
     * NESTED和REQUIRED修饰的内部方法都属于外围方法事务，如果外围方法抛出异常，
     * 这两种方法的事务都会被回滚。但是REQUIRED是加入外围方法事务，所以和外围事务同属于一个事务，
     * 一旦REQUIRED事务抛出异常被回滚，外围方法事务也将被回滚。而NESTED是外围方法的子事务，
     * 有单独的保存点，所以NESTED方法抛出异常被回滚，不会影响到外围方法的事务。
     *
     */
    @Transactional
    public void tranFather_v7() {
        userService.insertUser(User
                .builder()
                .name("父亲")
                .build());
        userService2.insertUserExceptionNested(User
                .builder()
                .name("儿子")
                .build());
        throw new RuntimeException();
    }

    /**
     * 异步执行
     * 事物的传播实现原理是基于ThreadLocal
     */
    @Transactional
    public void tranFather_v8() {
        userService.insertUser(User
                .builder()
                .name("异步").id(11L)
                .build());
        userService.findByIdAsync(11);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("我执行完毕");
    }

    /**
     * 同一事物中是可以读到的。
     * 由于mysql默认事物隔离配置的是REPEATABLE READ 可重复度
     *
     */
    @Transactional
    public void tranFather_v10() {
        userService.insertUser(User
                .builder()
                .name("父亲").id(11L)
                .build());
        userService2.findById(11);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("我执行完毕");
    }

    /**
     * 允许脏读
     */
    @Transactional
    public void tranFather_v9() {
        userService.insertUser(User
                .builder()
                .name("允许脏读").id(11L)
                .build());
        userService.findByIdAsyncUncommitted(11);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("我执行完毕");
    }
}

class MyException extends Exception {

}

class YourException extends Exception {

}
/**
 * 要想让事务嵌套起作用必须不能发在同一个类中
 * <p>
 * Propagation.REQUIRES_NEW
 */