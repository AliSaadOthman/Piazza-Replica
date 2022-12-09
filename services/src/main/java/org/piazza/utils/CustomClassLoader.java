package org.piazza.utils;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
@Service
public class CustomClassLoader extends ClassLoader{

    private final String CLASS_EXT = ".class";
    private final char PACKAGE_SEPARATOR = '.';

    private byte[] readClassFileAsBytes(String className) throws IOException {
        byte[] buffer;
        try (final InputStream inputStream = getClassInputStream(className)) {
            if (inputStream == null) {
                throw new IOException("Class file not found.");
            }
            buffer = inputStream.readAllBytes();
        }
        return buffer;
    }

    private InputStream getClassInputStream(String classPath) {
        final String className = classPath.replace(PACKAGE_SEPARATOR, File.separatorChar) + CLASS_EXT;
        System.out.println(this.getClass().toGenericString());
        return this.getClass().getClassLoader().getResourceAsStream(className);
    }

    public Class<?> loadClassFromBytes(String name, byte[] classBytes) {
        return new CustomClassLoader().defineClass(name, classBytes, 0, classBytes.length);
    }

    public Class<?> loadClassByName(String name) throws ClassNotFoundException {
        try {
            final byte[] classBytes = readClassFileAsBytes(name);
            return loadClassFromBytes(name, classBytes);
        } catch (IOException e) {
            throw new ClassNotFoundException("Class not found.", e);
        }
    }
}