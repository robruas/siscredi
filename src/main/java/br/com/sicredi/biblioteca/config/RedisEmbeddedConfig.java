package br.com.sicredi.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@Configuration
public class RedisEmbeddedConfig {

    private RedisServer redisServer;

    @Bean
    RedisServer redisServer() throws Exception {
        redisServer = new RedisServer(6379);
        redisServer.start();
        return redisServer;
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
