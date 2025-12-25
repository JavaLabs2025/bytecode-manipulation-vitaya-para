package org.example.metrics;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Set;

public class ComplexityAnalyzer {

    private static final Set<Integer> ASSIGNMENT_OPCODES = Set.of(
            Opcodes.ISTORE, Opcodes.LSTORE, Opcodes.FSTORE, Opcodes.DSTORE, Opcodes.ASTORE,
            Opcodes.PUTFIELD, Opcodes.PUTSTATIC,
            Opcodes.IASTORE, Opcodes.LASTORE, Opcodes.FASTORE, Opcodes.DASTORE,
            Opcodes.AASTORE, Opcodes.BASTORE, Opcodes.CASTORE, Opcodes.SASTORE
    );

    private static final Set<Integer> BRANCH_OPCODES = Set.of(
            Opcodes.IFEQ, Opcodes.IFNE, Opcodes.IFLT, Opcodes.IFGE, Opcodes.IFGT, Opcodes.IFLE,
            Opcodes.IF_ICMPEQ, Opcodes.IF_ICMPNE, Opcodes.IF_ICMPLT, Opcodes.IF_ICMPGE,
            Opcodes.IF_ICMPGT, Opcodes.IF_ICMPLE, Opcodes.IF_ACMPEQ, Opcodes.IF_ACMPNE,
            Opcodes.IFNULL, Opcodes.IFNONNULL,
            Opcodes.LOOKUPSWITCH, Opcodes.TABLESWITCH
    );

    private static final Set<Integer> CONDITION_OPCODES = Set.of(
            Opcodes.LCMP, Opcodes.FCMPL, Opcodes.FCMPG, Opcodes.DCMPL, Opcodes.DCMPG,
            Opcodes.INSTANCEOF
    );


    /**
     *  a - assignments
     *  b - branches
     *  c - conditions
     *
     *  res = sqrt(a^2+b^2+c^2)
     */
    public double calculateAbcForClass(ClassNode classNode) {
        int totalAssignments = 0;
        int totalBranches = 0;
        int totalConditions = 0;

        for (MethodNode method : classNode.methods) {
            AbcCounts counts = countAbcMetrics(method);
            totalAssignments += counts.assignments;
            totalBranches += counts.branches;
            totalConditions += counts.conditions;
        }

        return Math.sqrt(
            totalAssignments * totalAssignments +
            totalBranches * totalBranches +
            totalConditions * totalConditions
        );
    }

    private AbcCounts countAbcMetrics(MethodNode method) {
        int assignments = 0;
        int branches = 0;
        int conditions = 0;

        for (AbstractInsnNode instruction : method.instructions) {
            int opcode = instruction.getOpcode();

            if (ASSIGNMENT_OPCODES.contains(opcode)) {
                assignments++;
            }
            if (BRANCH_OPCODES.contains(opcode)) {
                branches++;
            }
            if (CONDITION_OPCODES.contains(opcode)) {
                conditions++;
            }
        }

        return new AbcCounts(assignments, branches, conditions);
    }

    private record AbcCounts(int assignments, int branches, int conditions) {}
}