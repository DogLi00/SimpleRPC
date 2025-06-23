package com.upc.protocal;

import com.upc.register.RPCApplicationRegister;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    public String send(Invocation invocation) {
        HttpURLConnection con = null;
        BufferedReader reader = null;

        try {
            // 1. 获取服务URL
            URL url = RPCApplicationRegister.get(invocation.getInterfaceName());

            // 2. 创建并配置连接
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");

            // 3. 发送请求数据
            try (OutputStream os = con.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
                String json = convertInvocationToJson(invocation);
                writer.write(json);
                writer.flush();
            }

            // 4. 处理响应
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP error code: " + responseCode);
            }

            // 5. 读取响应
            StringBuilder response = new StringBuilder();
            try (InputStream is = con.getInputStream();
                 InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();

        } catch (Exception e) {
            throw new RuntimeException("RPC调用失败", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String convertInvocationToJson(Invocation invocation) {
        // 使用FastJSON将Invocation对象转为JSON字符串
        return com.alibaba.fastjson.JSON.toJSONString(invocation);
    }
}