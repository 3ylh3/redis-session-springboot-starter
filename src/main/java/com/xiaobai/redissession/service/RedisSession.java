package com.xiaobai.redissession.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * redisSession接口
 *
 * @author yin_zhj
 * @date 2020/6/1
 */
public interface RedisSession {
    void set(HttpServletResponse response, Map<String, String> map);
    String get(HttpServletRequest request, String key);
    void removeSession(HttpServletRequest request, HttpServletResponse response);
    boolean checkSession(HttpServletRequest request);
}
