package com.upc.redisConfig;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    private static JedisPool jedisPool;

    // 初始化连接池
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);

        jedisPool = new JedisPool(poolConfig, "******", 6379, 2000, null);
    }

    // 获取Jedis实例
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    // 关闭Jedis连接
    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    // 获取字符串值
    public static String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    // 设置字符串值
    public static void set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.set(key, value);
        }
    }

    // 设置带过期时间的字符串值
    public static void setex(String key, int seconds, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.setex(key, seconds, value);
        }
    }

    // 删除键
    public static void del(String key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(key);
        }
    }

    // 检查键是否存在
    public static boolean exists(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        }
    }

    // 关闭连接池
    public static void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public static void setObject(String key, Object obj) {
        String jsonStr = JSON.toJSONString(obj);
        Jedis jedis = getJedis();
        jedis.set(key, jsonStr);
    }

    public static String getObjectJSONString(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

}
