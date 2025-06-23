package com.upc;

import com.upc.protocal.ConsumerProxy;

public class ConsumerApp {
    public static void main(String[] args) {
        /**
         * 目的代码。实现RPC的远程方法调用的最终格式。
         */
        HelloService helloService = ConsumerProxy.getProxy(HelloService.class);
        String result = helloService.sayHello("dog");
        System.out.println(result);
    }
}
