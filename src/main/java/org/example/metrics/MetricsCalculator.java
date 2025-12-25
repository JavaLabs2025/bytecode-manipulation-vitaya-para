package org.example.metrics;

import org.objectweb.asm.tree.ClassNode;

import java.util.Map;


public class MetricsCalculator {

    public record MetricsResult(int maxInheritanceDepth, double avgInheritanceDepth, double abcMetric,
                                double avgOverriddenMethods, double avgFieldsPerClass) {

    }

    private final Map<String, ClassNode> classes;
    private final InheritanceAnalyzer inheritanceAnalyzer;
    private final MethodAnalyzer methodAnalyzer;
    private final FieldAnalyzer fieldAnalyzer;
    private final ComplexityAnalyzer complexityAnalyzer;

    public MetricsCalculator(Map<String, ClassNode> classes) {
        this.classes = classes;
        this.inheritanceAnalyzer = new InheritanceAnalyzer(classes);
        this.methodAnalyzer = new MethodAnalyzer(classes);
        this.fieldAnalyzer = new FieldAnalyzer();
        this.complexityAnalyzer = new ComplexityAnalyzer();
    }

    public MetricsResult calculateMetrics() {
        int maxDepth = 0;
        int sumDepth = 0;
        int totalOverrides = 0;
        int totalFields = 0;
        double totalAbc = 0;

        for (ClassNode classNode : classes.values()) {
            int depth = inheritanceAnalyzer.calculateInheritanceDepth(classNode);
            if (depth > maxDepth) {
                maxDepth = depth;
            }
            sumDepth += depth;

            int overrides = methodAnalyzer.countOverriddenMethods(classNode);
            totalOverrides += overrides;

            int fields = fieldAnalyzer.countFields(classNode);
            totalFields += fields;

            double abc = complexityAnalyzer.calculateAbcForClass(classNode);
            totalAbc += abc;
        }

        int classCount = classes.size();
        double avgDepth = (double) sumDepth / classCount;
        double avgOverrides = (double) totalOverrides / classCount;
        double avgFields = (double) totalFields / classCount;

        return new MetricsResult(maxDepth, avgDepth, totalAbc, avgOverrides, avgFields);
    }
}