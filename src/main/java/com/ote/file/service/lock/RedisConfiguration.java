package com.ote.file.service.lock;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnProperty(value = "redis.enabled", havingValue = "true")
@EnableConfigurationProperties(RedisConfiguration.Redis.class)
@Slf4j
public class RedisConfiguration {

    public RedisConfiguration() {
        log.warn("#### Redis enabled ###");
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(@Autowired Redis redis) {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redis.getHost(), redis.getPort()));
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(@Autowired RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "redis")
    public static class Redis {

        private String host;

        private Integer port;
    }
}


