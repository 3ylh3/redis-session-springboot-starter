package com.xiaobai.redissession.config;

import com.xiaobai.redissession.exception.ExpireTypeException;
import com.xiaobai.redissession.service.RedisSession;
import com.xiaobai.redissession.service.impl.RedisSessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配类
 *
 * @author yin_zhj
 * @date 2020/6/1
 */
@Configuration
@EnableConfigurationProperties(RedisSessionProperties.class)
public class RedisSessionAutoConfigure {
    @Autowired
    private RedisSessionProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public RedisSession init() {
        return new RedisSessionImpl(properties.getExpireType(), properties.getExpire());
    }
}
