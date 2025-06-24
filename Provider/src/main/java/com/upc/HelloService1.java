package com.upc;

import com.upc.annotation.ProviderClass;
import com.upc.annotation.ProviderMethod;



public class HelloService1 implements HelloService{
    public String sayHello(String name) {
        return "HelloService01: "+name;
    }
}
