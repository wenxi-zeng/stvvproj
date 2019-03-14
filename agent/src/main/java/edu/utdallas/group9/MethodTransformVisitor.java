package edu.utdallas.group9;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {

    String mName;
    int line;
    private HashSet<Integer> visitedLines;
    private String className;

    public MethodTransformVisitor(final MethodVisitor mv, String name, String className) {
        super(Opcodes.ASM5, mv);
        this.mName = name;
        this.className = className;
        visitedLines = new HashSet<>();
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        this.line = i;
        record(i);
        super.visitLineNumber(i, label);
    }

    @Override
    public void visitLabel(Label label) {
        record(line);
        super.visitLabel(label);
    }

    private void record(int line) {
        if (!visitedLines.add(line)) return;
        if (mName.contains("<")) return;
        String temp = className + "." + mName;
        mv.visitLdcInsn(temp);
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