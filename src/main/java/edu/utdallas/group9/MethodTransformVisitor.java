package edu.utdallas.group9;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	String mName;
	int line;
	
    public MethodTransformVisitor(final MethodVisitor mv, String name) {
        super(ASM5, mv);
        this.mName=name;
    }

    // method coverage collection
    @Override
    public void visitCode(){
        print(mName+" executed");
    	super.visitCode();
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        super.visitLineNumber(i, label);
        line = i;
        CoverageManager.getInstance().addStatementCoverage(mName, i);
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
    }

    private void print(String message) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(message);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
 }