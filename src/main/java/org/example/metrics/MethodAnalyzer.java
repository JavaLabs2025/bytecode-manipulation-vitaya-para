package org.example.metrics;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MethodAnalyzer {

    private final Map<String, ClassNode> classes;

    public MethodAnalyzer(Map<String, ClassNode> classes) {
        this.classes = classes;
    }

    public int countOverriddenMethods(ClassNode classNode) {
        Set<MethodKey> superMethods = collectSuperMethods(classNode);
        int overrideCount = 0;

        for (MethodNode method : classNode.methods) {
            MethodKey key = new MethodKey(method.name, method.desc);
            if (superMethods.contains(key)) {
                overrideCount++;
            }
        }

        return overrideCount;
    }

    private Set<MethodKey> collectSuperMethods(ClassNode classNode) {
        Set<MethodKey> methods = new HashSet<>();

        if (classNode.interfaces != null) {
            for (String interfaceName : classNode.interfaces) {
                ClassNode interfaceNode = classes.get(interfaceName);
                addMethodsFromClass(interfaceNode, methods);
            }
        }

        ClassNode current = classes.get(classNode.superName);
        while (current != null && !current.name.equals("java/lang/Object")) {
            for (MethodNode method : current.methods) {
                methods.add(new MethodKey(method.name, method.desc));
            }
            current = classes.get(current.superName);
        }

        return methods;
    }

    private void addMethodsFromClass(ClassNode classNode, Set<MethodKey> methods) {
        if (classNode == null) {
            return;
        }
        for (MethodNode method : classNode.methods) {
            methods.add(new MethodKey(method.name, method.desc));
        }
    }

    public record MethodKey(String name, String descriptor) {

        @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof MethodKey)) return false;
                MethodKey that = (MethodKey) o;
                return name.equals(that.name) && descriptor.equals(that.descriptor);
            }

    }
}