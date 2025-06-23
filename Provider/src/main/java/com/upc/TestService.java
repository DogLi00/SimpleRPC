package com.upc;


import com.upc.protocal.HttpServer;
import com.upc.register.RPCApplicationRegister;
import com.upc.register.RPCMethodRegister;

import java.net.MalformedURLException;
import java.net.URL;

public class TestService {
    public static void main(String[] args) {

        RPCMethodRegister.put(HelloService.class.getName(), HelloService1.class);
        try {
            RPCApplicationRegister.put(HelloService.class.getName(), new URL("http://localhost:8080"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost",8080);
    }
}
