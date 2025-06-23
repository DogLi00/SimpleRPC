package com.upc.register;

import com.alibaba.fastjson.JSON;
import com.upc.redisConfig.RedisUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RPCApplicationRegister {
    public static URL get(String interfaceName) {
        String urlString = RedisUtil.get(interfaceName);
        return JSON.parseObject(urlString, URL.class);
    }


    public static void put(String interfaceName, URL url) {
        RedisUtil.set(interfaceName, JSON.toJSONString(url));
    }
}
