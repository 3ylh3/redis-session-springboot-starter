package com.xiaobai.redissession.service.impl;

import com.xiaobai.redissession.exception.ExpireTypeException;
import com.xiaobai.redissession.service.RedisSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redisSession实现类
 *
 * @author yin_zhj
 * @date 2020/6/1
 */
public class RedisSessionImpl implements RedisSession {
    private static final String ID = "rSessionId";
    private static final String PATH = "/";

    private TimeUnit expireType;
    private Long expire;

    private Logger logger = LoggerFactory.getLogger("com.xiaobai.redissession.service.impl.RedisSessionImpl");

    public RedisSessionImpl(String expireType, Long expire) {
        this.expire = expire;
        switch (expireType) {
            case "DAYS":
                this.expireType = TimeUnit.DAYS;
                break;
            case "HOURS":
                this.expireType = TimeUnit.HOURS;
                break;
            case "MINUTES":
                this.expireType = TimeUnit.MINUTES;
                break;
            case "SECONDS":
                this.expireType = TimeUnit.SECONDS;
                break;
            default:
                throw new ExpireTypeException();
        }
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(HttpServletResponse response, Map<String, String> map) {
        logger.info("set session start......");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String jSessionId = sf.format(new Date());
        logger.info("session id:{}", jSessionId);
        redisTemplate.opsForHash().putAll(jSessionId, map);
        redisTemplate.expire(jSessionId, expire, expireType);
        Cookie cookie = new Cookie(ID, jSessionId);
        response.addCookie(cookie);
        logger.info("set session end");
    }

    @Override
    public String get(HttpServletRequest request, String key) {
        logger.info("get session start......");
        Cookie[] cookies = request.getCookies();
        String jSessionId = null;
        if(null != cookies) {
            for(Cookie cookie : cookies) {
                if(ID.equals(cookie.getName())) {
                    jSessionId = cookie.getValue();
                }
            }
        }
        if(null != jSessionId) {
            logger.info("session id:{}", jSessionId);
            if(!redisTemplate.opsForHash().hasKey(jSessionId, key)) {
                logger.info("key:{},value:{}", key, null);
                logger.info("get session end");
                return null;
            } else {
                String value = (String)redisTemplate.opsForHash().get(jSessionId, key);
                logger.info("key:{},value:{}", key, value);
                logger.info("get session end");
                return value;
            }
        } else {
            logger.info("session is null");
            logger.info("get session end");
            return null;
        }
    }

    @Override
    public void removeSession(HttpServletRequest request, HttpServletResponse response) {
        logger.info("remove session...");
        Cookie[] cookies = request.getCookies();
        String jSessionId = null;
        if(null != cookies) {
            for(Cookie cookie : cookies) {
                if(ID.equals(cookie.getName())) {
                    jSessionId = cookie.getValue();
                    break;
                }
            }
            if(null != jSessionId) {
                logger.info("session id:{}", jSessionId);
                redisTemplate.delete(jSessionId);
                Cookie cookie = new Cookie(ID, null);
                cookie.setMaxAge(0);
                cookie.setPath(PATH);
                response.addCookie(cookie);
            }
        }
        logger.info("remove session end");
    }

    @Override
    public boolean checkSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jSessionId = null;
        if(null != cookies) {
            for(Cookie cookie : cookies) {
                if(ID.equals(cookie.getName())) {
                    jSessionId = cookie.getValue();
                    break;
                }
            }
            if(null != jSessionId) {
                if(redisTemplate.hasKey(jSessionId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
