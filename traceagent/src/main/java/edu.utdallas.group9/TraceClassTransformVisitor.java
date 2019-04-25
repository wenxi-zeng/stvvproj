package edu.utdallas.group9;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TraceClassTransformVisitor extends ClassVisitor implements Opcodes {

    private String className;

    public TraceClassTransformVisitor(final ClassVisitor cv, final String className) {
        super(Opcodes.ASM5, cv);
        this.className = className.replaceAll("/", ".");
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return mv == null ? null : new VariableTracker(mv, name, desc, className, access);
    }
}
