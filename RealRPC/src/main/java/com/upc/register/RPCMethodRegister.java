package com.upc.register;

import com.alibaba.fastjson.JSON;
import com.upc.redisConfig.RedisUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RPCMethodRegister {

    private static Map<String,Class> map = new HashMap<>();

    public static void put(String interfaceName,Class implClass){
        map.put(interfaceName, implClass);
    }

    public static Class get(String interfaceName){
        return map.get(interfaceName);
    }

}
