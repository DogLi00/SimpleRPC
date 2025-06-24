package com.upc.annotation;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JavaProviderClassScanner {

    public static Map<Class<?>, Class<?>> scan(String basePackage) throws Exception {
        Map<Class<?>, Class<?>> result = new HashMap<>();
        String path = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                scanDirectory(new File(resource.getFile()), basePackage, result);
            }
            // 可以添加处理jar文件的逻辑
        }

        return result;
    }

    private static void scanDirectory(File directory, String packageName, Map<Class<?>, Class<?>> result) {
        if (!directory.exists()) return;

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), result);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(ProviderClass.class)) {
                        ProviderClass annotation = clazz.getAnnotation(ProviderClass.class);
                        result.put(clazz, annotation.value());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Map<Class<?>, Class<?>> providers = scan("com.your.package");
        providers.forEach((providerClass, interfaceClass) -> {
            System.out.println(providerClass.getName() + " → " + interfaceClass.getName());
        });
    }
}