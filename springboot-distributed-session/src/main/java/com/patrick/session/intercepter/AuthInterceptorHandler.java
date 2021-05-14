package com.patrick.session.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * @date: Created in 2021/5/11 3:25 下午
 * @desc:
 * @motto: Keep It Simple and Stupid, KISS
 */
public class AuthInterceptorHandler implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!ObjectUtils.isEmpty(request.getSession().getAttribute("user"))) {
			return true;
		}
		print(response, "您没有权限访问！请先登录.");
		return false;
	}

	protected void print(HttpServletResponse response, String message) throws Exception {
		/**
		 * 设置响应头
		 */
		response.setHeader("Content-Type", "application/json");
		response.setCharacterEncoding("UTF-8");
		String result = new ObjectMapper().writeValueAsString(message);
		response.getWriter().print(result);

	}
}
