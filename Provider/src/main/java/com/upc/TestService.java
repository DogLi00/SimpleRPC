package com.upc;


import com.upc.protocal.HttpServer;

public class TestService {


    public static void main(String[] args) throws Exception {

        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost",8080);
    }
}
