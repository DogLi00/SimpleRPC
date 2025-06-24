package com.upc.protocal;

import com.upc.annotation.ConsumerInterface;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConsumerBoost {
    public static void start() throws Exception {
        // 扫描所有带有@ConsumerInterface注解的静态字段
        List<Field> consumerFields = scanConsumerFields("com.upc");

        // 为每个字段创建代理并注入
        for (Field field : consumerFields) {
            Object proxy = ConsumerProxy.getProxy(field.getType());
            field.setAccessible(true);
            field.set(null, proxy); // 设置静态字段值
        }
    }

    private static List<Field> scanConsumerFields(String packageName) throws Exception {
        List<Field> fields = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');

        for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
            if (resource.getProtocol().equals("file")) {
                scanDirectory(new java.io.File(resource.getFile()), packageName, fields);
            }
        }

        return fields;
    }

    private static void scanDirectory(java.io.File directory, String packageName, List<Field> fields)
            throws ClassNotFoundException {
        java.io.File[] files = directory.listFiles();
        if (files == null) return;

        for (java.io.File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), fields);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field.isAnnotationPresent(ConsumerInterface.class)
                                && java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                            fields.add(field);
                        }
                    }
                } catch (NoClassDefFoundError e) {
                    // 忽略无法加载的类
                }
            }
        }
    }
}