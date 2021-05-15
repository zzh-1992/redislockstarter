package com.grapefruit.redislockstarter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Grapefruit
 * @version 1.0
 * @date 2021/4/26
 */
@Component
@ConfigurationProperties(prefix="jedis")
@Data
public class JedisProperties {

    // SpringBoot 官网链接:https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-configuration-metadata.html#configuration-metadata-annotation-processor
    /**
     * password of the redis.
     */
    private String password;

    /**
     * IP address to listen to.
     */
    private String ip;

    /**
     * Port to listener to.
     */
    private int port;
}
