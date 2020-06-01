package com.xiaobai.redissession.exception;

/**
 * 过期时间类型异常
 *
 * @author yin_zhj
 * @date 2020/6/1
 */
public class ExpireTypeException extends RuntimeException {
    public ExpireTypeException() {
        super("Expire type error");
    }
}
