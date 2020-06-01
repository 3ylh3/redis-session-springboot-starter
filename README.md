# Redis-Session
redisSession是一个使用redis存储session数据的简易springboot-starter。
# 如何使用
1.引入依赖
```xml
install到本地库后引入依赖
<dependency>
    <groupId>com.xiaobai</groupId>
    <artifactId>redis-session-springboot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
2.配置redis连接和session超时时间
```
#redis服务器地址
spring.redis.host=xxx.xxx.xxx.xxx
#redis端口
spring.redis.port=6379
#连接池最大连接数
spring.redis.jedis.pool.max-active=10
#连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.jedis.pool.max-wait=-1
#连接池中的最大空闲连接
spring.redis.jedis.pool.max-idle=5
#连接池中的最小空闲连接
spring.redis.jedis.pool.min-idle=0
#session超时时间
redis-session.expire=24
#session超时时间类型：DAYS:天 HOURS:小时 MINUTES:分 SECONDS:秒
redis-session.expireType=HOURS
```
3.程序中引入redisSession
```java
@Autowired
private RedisSession redisSession;
```
5.设置session
```java
redisSession.set(response, map);
```
入参为HttpServletResponse和存储session信息的map。
6.获取session信息
```java
String message = redisSession.get(request, key)
```
入参为HttpServletRequest和session中对应的key。
7.检查session是否存在
```java
redisSession.checkSession(request)
```
入参为HttpServletRequest，返回true则session存在，返回false则session不存在。
8.移除session
```java
redisSession.removeSession(request, response);
```
入参为HttpServletRequest和HttpServletResponse。
# 下载
https://github.com/3ylh3/redis-session-springboot-starter/releases
