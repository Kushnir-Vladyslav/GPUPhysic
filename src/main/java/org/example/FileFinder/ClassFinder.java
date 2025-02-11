package org.example.FileFinder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

public class ClassFinder {

    public static Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("\\");

            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                File resourcePath;

                try {
                    resourcePath = new File(resourceUrl.toURI());
                } catch (Exception e) {
                    continue;
                }

                Class<?> clazz = scanClasses(resourcePath, className, "");

                if (clazz != null) {
                    return clazz;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new ClassNotFoundException("Class " + className + " not found among subclasses of Library");
    }

    private static Class<?> scanClasses (File directory, String className, String packageName) throws ClassNotFoundException {
        if (!directory.exists()) {
            return null;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                Class<?> clazz = scanClasses(file, className, packageName + file.getName() + ".");
                if (clazz != null) {
                    return clazz;
                }
            } else {
                if (file.getName().endsWith(className + ".class")) {
                    String fullClassName = packageName + file.getName().replace(".class", "");
                    return Class.forName(fullClassName);
                }
            }
        }

        return null;
    }
}
