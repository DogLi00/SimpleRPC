package com.upc.redisConfig;

public class RedisTest {
    public static void main(String[] args) {
        RedisUtil.set("hello","word");
        System.out.printf(RedisUtil.get("hello"));
    }
}
