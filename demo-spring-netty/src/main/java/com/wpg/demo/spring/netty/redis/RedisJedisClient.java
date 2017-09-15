package com.wpg.demo.spring.netty.redis;

import redis.clients.jedis.Jedis;

/**
 * @author ChangWei Li
 * @version 2017-09-15 16:32
 */
public class RedisJedisClient {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.99.100", 6379);
        String result = jedis.get("test");
        System.out.println(result);
        jedis.close();
    }

}
