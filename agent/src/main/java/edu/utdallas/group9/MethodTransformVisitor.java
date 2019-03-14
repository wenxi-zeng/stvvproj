package edu.utdallas.group9;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {

    String mName;
    int line;
    private HashSet<Integer> visitedLines;

    public MethodTransformVisitor(final MethodVisitor mv, String name) {
        super(Opcodes.ASM5, mv);
        this.mName = name;
        visitedLines = new HashSet<>();
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        this.line = i;
        if (visitedLines.add(i)) {
            record(i);
        }
        super.visitLineNumber(i, label);
    }

    @Override
    public void visitLabel(Label label) {
        if (visitedLines.add(line)) {
            record(line);
        }
        super.visitLabel(label);
    }

    private void record(int line) {
        mv.visitLdcInsn(mName);
        mv.visitLdcInsn(new Integer(line));
        mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/group9/CoverageManager", "newStatementCoverage", "(Ljava/lang/String;I)V", false);
    }

    public HashSet<Integer> getVisitedLines() {
        return visitedLines;
    }

    public void setVisitedLines(HashSet<Integer> visitedLines) {
        this.visitedLines = visitedLines;
    }
}