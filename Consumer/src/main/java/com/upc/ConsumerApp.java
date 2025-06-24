package com.upc;

import com.upc.annotation.ConsumerInterface;
import com.upc.protocal.ConsumerBoost;

public class ConsumerApp {

    @ConsumerInterface
    static HelloService helloService;

    public static void main(String[] args) throws Exception {
        /**
         * 目的代码。实现RPC的远程方法调用的最终格式。
         */
        ConsumerBoost.start();
        String result = helloService.sayHello("dog");
        System.out.println(result);
    }
}
