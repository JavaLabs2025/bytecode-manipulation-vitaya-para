package org.example.metrics;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Set;

public class ComplexityAnalyzer {

    private static final Set<Integer> STORE_OPCODES = Set.of(
            Opcodes.ISTORE,
            Opcodes.LSTORE,
            Opcodes.FSTORE,
            Opcodes.DSTORE,
            Opcodes.ASTORE
    );


    public int calculateAbcForClass(ClassNode classNode) {
        int totalAssignments = 0;

        for (MethodNode method : classNode.methods) {
            totalAssignments += countAssignments(method);
        }

        return totalAssignments;
    }

    private int countAssignments(MethodNode method) {
        int count = 0;

        for (AbstractInsnNode instruction : method.instructions) {
            if (STORE_OPCODES.contains(instruction.getOpcode())) {
                count++;
            }
        }

        return count;
    }

    public int calculateCyclomaticComplexity(MethodNode method) {
        int complexity = 1;

        for (AbstractInsnNode instruction : method.instructions) {
            int opcode = instruction.getOpcode();

            if (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) {
                complexity++;
            }
            else if (opcode == Opcodes.LOOKUPSWITCH || opcode == Opcodes.TABLESWITCH) {
                complexity++;
            }
            else if (opcode == Opcodes.GOTO) {
                complexity++;
            }
        }

        return complexity;
    }
}