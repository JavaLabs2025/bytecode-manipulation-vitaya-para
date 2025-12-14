package org.example.metrics;

import org.objectweb.asm.tree.ClassNode;

import java.util.Map;

public class DetailedMetricsCalculator {

    private final Map<String, ClassNode> classes;
    private final InheritanceAnalyzer inheritanceAnalyzer;
    private final MethodAnalyzer methodAnalyzer;
    private final FieldAnalyzer fieldAnalyzer;
    private final ComplexityAnalyzer complexityAnalyzer;

    public DetailedMetricsCalculator(Map<String, ClassNode> classes) {
        this.classes = classes;
        this.inheritanceAnalyzer = new InheritanceAnalyzer(classes);
        this.methodAnalyzer = new MethodAnalyzer(classes);
        this.fieldAnalyzer = new FieldAnalyzer();
        this.complexityAnalyzer = new ComplexityAnalyzer();
    }

    public MetricsCalculator.MetricsResult calculateMetricsWithDetails() {
        System.out.println("\n===DEBUG===\n");

        int maxDepth = 0;
        int sumDepth = 0;
        int totalOverrides = 0;
        int totalFields = 0;
        int totalAbc = 0;

        for (ClassNode classNode : classes.values()) {
            System.out.println("class: " + classNode.name);

            int depth = inheritanceAnalyzer.calculateInheritanceDepth(classNode);
            System.out.println("depth: " + depth);
            if (depth > maxDepth) {
                maxDepth = depth;
            }
            sumDepth += depth;

            int overrides = methodAnalyzer.countOverriddenMethods(classNode);
            System.out.println("overrides: " + overrides);
            totalOverrides += overrides;

            int fields = fieldAnalyzer.countFields(classNode);
            System.out.println("fields: " + fields);
            if (classNode.fields != null && !classNode.fields.isEmpty()) {
                System.out.println(
                    "fields: " + classNode.fields.stream()
                    .map(f -> f.name + ":" + f.desc)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("")
                );
            }
            totalFields += fields;

            int abc = complexityAnalyzer.calculateAbcForClass(classNode);
            System.out.println("abc: " + abc);
            totalAbc += abc;

            System.out.println();
        }

        int classCount = classes.size();
        double avgDepth = (double) sumDepth / classCount;
        double avgOverrides = (double) totalOverrides / classCount;
        double avgFields = (double) totalFields / classCount;

        System.out.println("=== TOTAL ===");
        System.out.println("classCount: " + classCount);
        System.out.println("maxDepth: " + maxDepth);
        System.out.println("avgDepth: " + avgDepth);
        System.out.println("totalAbc: " + totalAbc);
        System.out.println("avgOverrides: " + avgOverrides);
        System.out.println("avgFields: " + avgFields);
        System.out.println();

        return new MetricsCalculator.MetricsResult(
                maxDepth, avgDepth, totalAbc, avgOverrides, avgFields
        );
    }
}