package org.example;

import org.example.metrics.MetricsCalculator;
import org.example.metrics.MetricsCalculator.MetricsResult;
import org.example.util.JarReader;
import org.example.util.MetricsWriter;
import org.example.visitor.ClassPrinter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Example {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: java Example <command> <jarFile> [options]");
            System.err.println("Commands:");
            System.err.println("  print <jarFile>              - Print class structure");
            System.err.println("  metrics <jarFile> [out.json] - Calculate metrics");
            return;
        }

        String command = args[0];

        switch (command.toLowerCase()) {
            case "print":
                if ( args.length < 2 ) {
                    System.err.println("Usage: java Example print <jarFile>");
                    return;
                }

                printJarClasses(args[1]);
                break;

            case "metrics":
                if ( args.length < 2 ) {
                    System.err.println("Usage: java Example metrics <jarFile> [out.json]");
                    return;
                }

                String jarPath = args[1];
                File outputFile = args.length > 2 ? new File(args[2]) : null;
                calculateMetrics(jarPath, outputFile);
                break;

            default:
                System.err.println("Unknown command: " + command);
                System.err.println("Available commands: print, metrics");
        }
    }

    private static void printJarClasses(String jarPath) throws IOException {
        try ( JarFile jarFile = new JarFile(jarPath) ) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    ClassPrinter printer = new ClassPrinter();
                    ClassReader reader = new ClassReader(jarFile.getInputStream(entry));
                    reader.accept(printer, 0);
                }
            }
        }
    }

    private static void calculateMetrics(String jarPath, File outputFile) {
        try {
            Map<String, ClassNode> classes = JarReader.readJar(jarPath);

            MetricsCalculator calculator = new MetricsCalculator(classes);
            MetricsResult result = calculator.calculateMetrics();

            MetricsWriter writer = new MetricsWriter();
            writer.writeMetrics(result, outputFile);

        } catch (Exception e) {
            System.err.println("Error analyzing JAR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}