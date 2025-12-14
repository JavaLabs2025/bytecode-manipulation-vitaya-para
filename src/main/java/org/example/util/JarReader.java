package org.example.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarReader {

    public static Map<String, ClassNode> readJar(String jarPath) throws IOException {
        Map<String, ClassNode> classes = new LinkedHashMap<>();

        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(jarPath))) {
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }

                byte[] classBytes = readAllBytes(zipStream);
                ClassNode classNode = new ClassNode();
                ClassReader classReader = new ClassReader(classBytes);
                classReader.accept(classNode, 0);

                classes.put(classNode.name, classNode);
            }
        }

        return classes;
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int bytesRead;

        while ( (bytesRead = inputStream.read(data)) != -1 ) {
            buffer.write(data, 0, bytesRead);
        }

        return buffer.toByteArray();
    }
}