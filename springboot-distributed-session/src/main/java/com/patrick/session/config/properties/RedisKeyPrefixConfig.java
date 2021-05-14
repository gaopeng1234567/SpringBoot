package com.patrick.session.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
 * @date: Created in 2021/5/11 2:24 下午
 * @desc:
 * @motto: Keep It Simple and Stupid, KISS
 */
@ConfigurationProperties(prefix = "redis.key")
@Data
public class RedisKeyPrefixConfig {

	private RedisKeyPrefixConfig.Prefix prefix;

	private RedisKeyPrefixConfig.Expire expire;

	@Data
	public static class Prefix {
		private String otpCode;

	}

	@Data
	public static class Expire {

		private Long otpCode;

	}

}