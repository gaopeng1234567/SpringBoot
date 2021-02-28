package com.patrick.mybatis.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.patrick.mybatis.mapper.UserMapper;
import com.patrick.mybatis.model.User;
import javafx.scene.control.Pagination;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService1 {

    @Resource
    private UserMapper userMapper;

    /**
     * 通过ID查找用户
     *
     * @param id
     * @return
     */
    public User findById(Integer id) {
        User user = userMapper.selectById(id);
        System.out.println(JSON.toJSONString(user, true));
        return user;
    }

    @Async()
    public User findByIdAsync(Integer id) {
        User user = userMapper.selectById(id);
        System.out.println(JSON.toJSONString(user, true));
        return user;
    }
    @Async
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User findByIdAsyncUncommitted(Integer id) {
        User user = userMapper.selectById(id);
        System.out.println(JSON.toJSONString(user, true));
        return user;
    }

    /**
     * 新增用户
     *
     * @param user
     */
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    /**
     * 修改用户
     *
     * @param user
     */
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    /**
     * 删除用户
     *
     * @param id
     */
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }

    public List findAllUser() {
        return userMapper.selectList(null);
    }

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    public IPage<User> selectUserPage(Page<User> page) {
        return userMapper.selectPage(page, null);
    }


    /**
     * 异常
     *
     * @param user
     * @return
     */
    public int insertUserException(User user) {
        userMapper.insert(user);
        throw new RuntimeException();
    }
}
