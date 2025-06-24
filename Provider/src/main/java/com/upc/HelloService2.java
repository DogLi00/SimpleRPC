package com.upc;

import com.upc.annotation.ProviderClass;

@ProviderClass(HelloService.class)
public class HelloService2 implements HelloService{
    public String sayHello(String name) {
        return "HelloService02: "+name;
    }
}
