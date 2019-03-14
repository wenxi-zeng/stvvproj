package edu.utdallas.group9;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	String mName;
	int line;
	
    public MethodTransformVisitor(final MethodVisitor mv, String name) {
	this.mName=name;
        super(Opcodes.ASM5, mv);
    }

    // method coverage collection
    @Override
    public void visitCode(){
        print(mName+" executed");
    	super.visitCode();
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        this.line = i;
	    
        CoverageManager.getInstance().addStatementCoverage(mName, i);
	mv.visitLdcInsn(mName);
	mv.visitLdcInsn(new Integer(i));
	mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
	mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/group9/CoverageManager", "addStatementCoverage", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);
	super.visitLineNumber(i, label);
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
    }

    private void print(String message) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(message);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
 }
