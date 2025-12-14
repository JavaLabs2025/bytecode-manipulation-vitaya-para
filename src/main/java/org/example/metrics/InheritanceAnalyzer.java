package org.example.metrics;

import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

public class InheritanceAnalyzer {

    private final Map<String, ClassNode> classes;
    private final Map<String, Integer> depthCache;

    public InheritanceAnalyzer(Map<String, ClassNode> classes) {
        this.classes = classes;
        this.depthCache = new HashMap<>();
    }

    public int calculateInheritanceDepth(ClassNode classNode) {
        Integer cached = depthCache.get(classNode.name);
        if (cached != null) {
            return cached;
        }

        int depth = 1;
        String superName = classNode.superName;

        while (superName != null && !superName.equals("java/lang/Object")) {
            ClassNode superClass = classes.get(superName);
            if (superClass == null) {
                break;
            }
            depth++;
            superName = superClass.superName;
        }

        depthCache.put(classNode.name, depth);
        return depth;
    }

    public void clearCache() {
        depthCache.clear();
    }
}