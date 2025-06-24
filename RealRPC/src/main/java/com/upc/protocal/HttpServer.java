package com.upc.protocal;

import com.upc.annotation.JavaProviderClassScanner;
import com.upc.register.RPCApplicationRegister;
import com.upc.register.RPCMethodRegister;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpServer {
    public void start(String hostname, int port) throws Exception {

        Map<Class<?>, Class<?>> scanResult = JavaProviderClassScanner.scan("com.upc");
        scanResult.forEach((aClass, aClass2) -> {
            RPCMethodRegister.put(aClass2.getName(), aClass);
            try {
                RPCApplicationRegister.put(aClass2.getName(), new URL("http://"+hostname+":"+port));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });

        // 1. 创建Tomcat实例
        Tomcat tomcat = new Tomcat();
        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");
        Connector connector = new Connector();
        connector.setPort(port);
        // 5. 创建Engine组件
        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostname);
        Host host = new StandardHost();
        host.setName(hostname);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new  Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath, "dispatcher", new DispatchServlet());
        context.addServletMappingDecoded("/*", "dispatcher");
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
