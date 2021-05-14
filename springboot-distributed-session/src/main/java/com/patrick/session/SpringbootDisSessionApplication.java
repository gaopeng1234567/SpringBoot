package com.patrick.session;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ｜                 ,;,,;;
 * ｜               ,;;'(      风
 * ｜     __      ,;;' ' \     流
 * ｜  /'  '\'~~'~' \ /'\.)    堪
 * ｜ ,;(      )    /  |.      比
 * ｜,;' \    /-.,,(   ) \     丶
 * ｜    ) /       ) / )|      城
 * ｜    ||        ||  \)      管
 * ｜   (_\       (_\          希
 * @author: 城管丶希
 * @date: Created in 2021/4/4 2:38 下午
 * @desc:
 * @motto: 莫遣只轮归海窟，仍留一箭射天山
 */
@SpringBootApplication
@MapperScan("com.patrick.session.mapper")
public class SpringbootDisSessionApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootDisSessionApplication.class, args);
	}
}
