package com.grapefruit.redislockstarter.myRedisLock;

import java.lang.annotation.*;

/**
 * redisLock标记
 * @author Grapefruit
 * @version 1.0
 * @date 2021/4/26
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * 锁的生存时间,默认5秒
     */
    int ttl() default 5;
}
