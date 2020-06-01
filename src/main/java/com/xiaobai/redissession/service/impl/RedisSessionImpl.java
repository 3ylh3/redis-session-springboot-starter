package com.xiaobai.redissession.service.impl;

import com.xiaobai.redissession.exception.ExpireTypeException;
import com.xiaobai.redissession.service.RedisSession;
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
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String jSessionId = sf.format(new Date());
        redisTemplate.opsForHash().putAll(jSessionId, map);
        redisTemplate.expire(jSessionId, expire, expireType);
        Cookie cookie = new Cookie(ID, jSessionId);
        response.addCookie(cookie);
    }

    @Override
    public String get(HttpServletRequest request, String key) {
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
            if(!redisTemplate.opsForHash().hasKey(jSessionId, key)) {
                return null;
            } else {
                return (String)redisTemplate.opsForHash().get(jSessionId, key);
            }
        } else {
            return null;
        }
    }

    @Override
    public void removeSession(HttpServletRequest request, HttpServletResponse response) {
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
                redisTemplate.delete(jSessionId);
                Cookie cookie = new Cookie(ID, null);
                cookie.setMaxAge(0);
                cookie.setPath(PATH);
                response.addCookie(cookie);
            }
        }
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
