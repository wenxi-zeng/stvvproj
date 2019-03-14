package edu.utdallas.group9;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {

    String mName;
    int line;

    public MethodTransformVisitor(final MethodVisitor mv, String name) {
        super(Opcodes.ASM5, mv);
        this.mName = name;
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
        mv.visitLdcInsn(mName);
        mv.visitLdcInsn(new Integer(line));
        mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/group9/CoverageManager", "newStatementCoverage", "(Ljava/lang/String;I)V", false);
    }
}
