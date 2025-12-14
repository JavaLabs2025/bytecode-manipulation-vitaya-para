package org.example.metrics;

import org.objectweb.asm.tree.ClassNode;

public class FieldAnalyzer {

    public int countFields(ClassNode classNode) {
        return classNode.fields.size();
    }
}