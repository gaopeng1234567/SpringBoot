package com.patrick.session.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

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
@TableName("demo")
@Data
public class User implements Serializable {

	@NotBlank(message = "电话号码不允许为空")
	@Length(max = 11, min = 11, message = "电话必须是11字符")
	@Pattern(regexp = "^1[3|4|5|8][0-9]\\d{8}$", message = "电话号码格式不正确")
	@TableId
	private String phone;

	@NotBlank(message = "动态校验码不允许为空")
	@Length(max = 6, min = 6, message = "校验码必须是6字符")
	@TableField("otpCode")
	private String otpCode;

	@NotBlank(message = "用户名不允许为空")
	@Length(max = 20, min = 4, message = "用户名长度必须在4-20字符之间")
	private String username;

	@NotBlank(message = "密码不允许为空")
	@Length(max = 20, min = 8, message = "密码长度必须在8-20字符之间")
	@Ignore
	private String password;


}
