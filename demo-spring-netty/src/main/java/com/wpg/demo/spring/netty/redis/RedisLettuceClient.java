package com.wpg.demo.spring.netty.redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.rx.RedisStringReactiveCommands;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ChangWei Li
 * @version 2017-09-15 16:08
 */
@Slf4j
public class RedisLettuceClient {

    public static void main(String[] args) throws InterruptedException {
        RedisClient redisClient = RedisClient.create("redis://192.168.99.100");
        RedisStringReactiveCommands<String, String> commands = redisClient.connect().reactive();

        commands.get("test").subscribe(log::info);

        Thread.currentThread().join();
    }

}
