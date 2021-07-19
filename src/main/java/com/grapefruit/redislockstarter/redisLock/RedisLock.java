package com.grapefruit.redislockstarter.redisLock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redisLock标记
 *
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
