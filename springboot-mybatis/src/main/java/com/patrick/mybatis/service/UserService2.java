package com.patrick.mybatis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.patrick.mybatis.mapper.UserMapper;
import com.patrick.mybatis.model.User;
import javafx.scene.control.Pagination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService2 {

    @Resource
    private UserMapper userMapper;

    /**
     * 通过ID查找用户
     *
     * @param id
     * @return
     */
    public User findById(Integer id) {
        return userMapper.selectById(id);
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertUserException(User user) {
        userMapper.insert(user);
        throw new RuntimeException();
    }

    /**
     * 异常
     *
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.NESTED)
    public int insertUserExceptionNested(User user) {
        return userMapper.insert(user);
    }
}
