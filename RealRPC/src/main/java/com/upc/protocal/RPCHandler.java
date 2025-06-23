package com.upc.protocal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.register.RPCMethodRegister;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RPCHandler {
    private final ObjectMapper objectMapper = new ObjectMapper(); // 用于JSON解析
    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        Invocation invocation = null;
        try {
            invocation =  objectMapper.readValue(req.getReader(), Invocation.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (invocation==null){
            return;
        }
        // 要根据接口名获取接口实现类。（去注册中心找，此处用redis实现）
        Class implClass = RPCMethodRegister.get(invocation.getInterfaceName());
        try {
            Method method = implClass.getDeclaredMethod(invocation.getMethodName(), invocation.getParamTypes());
            method.setAccessible(true); // 允许访问私有方法
            // 3. 创建实例 (如果是静态方法可跳过)
            Object instance = null;
            if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                try {
                    instance = implClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            try {
                Object result =  method.invoke(instance, invocation.getParamValues());
                try {
                    resp.getWriter().write(objectMapper.writeValueAsString(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
