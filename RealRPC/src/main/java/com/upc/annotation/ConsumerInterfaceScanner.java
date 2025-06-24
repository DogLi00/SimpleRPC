package com.upc.annotation;

import com.upc.annotation.ConsumerInterface;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConsumerInterfaceScanner {

    /**
     * 扫描指定包下所有带有@ConsumerInterface注解的静态字段对应的接口类型
     * @param packageName 要扫描的包名
     * @return 找到的所有消费者接口Class列表
     */
    public static List<Class<?>> scanConsumerInterfaces(String packageName) {
        List<Class<?>> interfaces = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 将包名转换为路径
        String path = packageName.replace('.', '/');

        try {
            // 获取包下的所有类
            for (Class<?> clazz : getClasses(classLoader, path)) {
                // 检查类中的字段
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ConsumerInterface.class)
                            && java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                        // 只添加接口类型
                        if (field.getType().isInterface()) {
                            interfaces.add(field.getType());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return interfaces;
    }

    /**
     * 辅助方法：获取包下的所有类
     */
    private static List<Class<?>> getClasses(ClassLoader classLoader, String path)
            throws Exception {
        List<Class<?>> classes = new ArrayList<>();

        for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
            if (resource.getProtocol().equals("file")) {
                File file = new File(resource.getFile());
                scanDirectory(file, path.replace('/', '.'), classes);
            }
        }

        return classes;
    }

    /**
     * 辅助方法：递归扫描目录
     */
    private static void scanDirectory(File directory, String packageName, List<Class<?>> classes)
            throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                try {
                    classes.add(Class.forName(className));
                } catch (NoClassDefFoundError e) {
                    // 忽略无法加载的类
                }
            }
        }
    }
}