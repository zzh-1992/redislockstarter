package com.grapefruit.redislockstarter;

import com.grapefruit.redislockstarter.redisLock.JedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.util.Optional;

/**
 * 向容器注入jedis
 *
 * @author Grapefruit
 * @version 1.0
 * @date 2021/4/26
 */
@ComponentScan(basePackages = {"com.grapefruit"})
@Configuration()
public class RedisLockStarterConfig {

    @Autowired
    JedisProperties properties = new JedisProperties();

    @Bean
    public Jedis jedis() {
        String host = Optional.ofNullable(properties.getIp()).orElse("127.0.0.1");
        int port = Optional.of(properties.getPort()).orElse(6369);
        Jedis jedis = new Jedis(host, port);
        jedis.auth(Optional.ofNullable(properties.getPassword()).orElse("6369"));
        return jedis;
    }
}
