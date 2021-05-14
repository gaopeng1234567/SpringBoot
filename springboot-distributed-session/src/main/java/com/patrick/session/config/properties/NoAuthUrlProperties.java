package com.patrick.session.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;


@Data
@ConfigurationProperties(prefix = "member.auth")
public class NoAuthUrlProperties {
	private LinkedHashSet<String> shouldSkipUrls;
}
