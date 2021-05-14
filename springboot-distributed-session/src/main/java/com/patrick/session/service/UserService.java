package com.patrick.session.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.patrick.session.config.properties.RedisKeyPrefixConfig;
import com.patrick.session.mapper.UserMapper;
import com.patrick.session.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * ｜                 ,;,,;;
 * ｜               ,;;'()     风
 * ｜     __      ,;;' ' \     流
 * ｜  /'  '\'~~'~' \ /'\.)    堪
 * ｜ ,;(      )    /  |.      比
 * ｜,;' \    /-.,,(   ) \     丶
 * ｜    ) /       ) / )|      城
 * ｜    ||        ||  \)      管
 * ｜   (_\       (_\          希
 * @author: 城管丶希
 * @date: Created in 2021/5/11 1:56 下午
 * @desc:
 * @motto: Keep It Simple and Stupid, KISS
 */
@Service
@EnableConfigurationProperties(RedisKeyPrefixConfig.class)
public class UserService {

	@Autowired
	private RedisTemplate redisTemplate;
	@Resource
	private RedisKeyPrefixConfig redisKeyPrefixConfig;

	@Resource
	UserMapper userMapper;
	@Resource
	private PasswordEncoder passwordEncoder;

	public String getCode(String phone) {
		User user = userMapper.selectById(phone);
		if (user != null)
			throw new RuntimeException();

		if (redisTemplate.hasKey(redisKeyPrefixConfig.getPrefix().getOtpCode() + phone)) {
			throw new RuntimeException();
		}
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(random.nextInt(10));
		}
		redisTemplate.opsForValue().set(redisKeyPrefixConfig.getPrefix().getOtpCode() + phone, sb.toString(),
				redisKeyPrefixConfig.getExpire().getOtpCode(), TimeUnit.SECONDS);
		return sb.toString();
	}

	public void register(User user) {
		String redisOtpCode = (String) redisTemplate.opsForValue().get(redisKeyPrefixConfig.getPrefix().getOtpCode() + user.getPhone());
		if (!StringUtils.hasLength(redisOtpCode) || !redisOtpCode.equals(user.getOtpCode())) {
			throw new RuntimeException("动态校验码不正确!");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.insert(user);
	}

	public User login(String username, String password) {
		QueryWrapper<User> wrapper = Wrappers.query();
		wrapper.eq("username", username);
		User user = userMapper.selectOne(wrapper);
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("用户名或密码不正确!");
		}
		return user;
	}
}
