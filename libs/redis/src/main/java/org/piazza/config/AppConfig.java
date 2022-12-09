package org.piazza.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration

class AppConfig {
    @Value("${connection.port}")
    private String port;
    @Value("${connection.host}")
    private String host;
    @Value("${connection.timeout}")
    private String timeOut;
    @Value("${connection.max-active}")
    private String maxActive;
    @Value("${connection.max-idle}")
    private String maxIdle;
    @Value("${connection.max-wait}")
    private String maxWait;
    @Value("${connection.min-idle}")
    private String minIdle;

    @Bean
    @Scope("singleton")
    public JedisPool getPooled(){

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(maxActive));
        poolConfig.setMaxIdle(Integer.parseInt(maxIdle));
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(1);
        JedisPool pool = new JedisPool(poolConfig,host, Integer.parseInt(port));
        return pool;
    }



}