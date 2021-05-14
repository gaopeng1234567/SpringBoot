package com.patrick.session.controller;

import com.patrick.session.model.User;
import com.patrick.session.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
 * @date: Created in 2021/5/11 1:54 下午
 * @desc:
 * @motto: Keep It Simple and Stupid, KISS
 */
@RestController()
@RequestMapping("/sso")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@GetMapping("/getCode")
	public String getCode(@RequestParam String phone) {
		return userService.getCode(phone);
	}

	@PostMapping("/register")
	public void register(@Valid @RequestBody User user) {
		userService.register(user);
	}

	@PostMapping("/login")
	public User login(@RequestParam String username, @RequestParam String password) {
		User user = userService.login(username, password);
		getHttpSession().setAttribute("user", user);
		return user;
	}

	@GetMapping("/test")
	public void getCode1() {
		System.out.println(getHttpSession().getAttribute("user"));
	}
}
