package com.upc;

import com.upc.annotation.JavaProviderClassScanner;

import java.util.List;
import java.util.Map;


public class ExposedProvider {
    public static void main(String[] args) throws Exception {
        System.out.println(HelloService.class.getName());
        Map<Class<?>, Class<?>> scan = JavaProviderClassScanner.scan("com.upc");
        scan.forEach((Class<?> aClass, Class<?> aClass2) -> System.out.println(aClass.getName()+"---"+aClass2.getName()));
    }
}
