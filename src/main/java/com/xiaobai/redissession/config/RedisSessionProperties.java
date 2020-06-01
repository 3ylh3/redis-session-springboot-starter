package com.xiaobai.redissession.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisSession配置类
 *
 * @author yin_zhj
 * @date 2020/6/1
 */
@Data
@ConfigurationProperties("redis-session")
public class RedisSessionProperties {
    /**
     * 过期时间类型
     */
    private String expireType;
    /**
     * 过期时间
     */
    private Long expire;
}
