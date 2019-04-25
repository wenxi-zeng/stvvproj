package edu.utdallas.group9;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TraceClassTransformVisitor extends ClassVisitor implements Opcodes {

    private String className;

    private ClassNode classNode;

    public TraceClassTransformVisitor(final ClassVisitor cv, final String className) {
        super(Opcodes.ASM5, cv);
        this.className = className.replaceAll("/", ".");
        this.classNode = new ClassNode();

        try {
            ClassReader reader = new ClassReader(className);
            reader.accept(classNode, 0);
        } catch (IOException e) {
            System.out.println("Failed to load " + className);
        }
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        String[] localVariableNames = null;
        for (final MethodNode mn : (List<MethodNode>)classNode.methods) {
            if ( mn.name.equals(name)) {
                //System.out.println("mn.localVariables.size():" +  mn.localVariables.size());
                localVariableNames = new String[mn.localVariables.size()];
                for (LocalVariableNode n : (List<LocalVariableNode>) mn.localVariables) {
                    localVariableNames[n.index] = n.name;
                }
            }
        }
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return mv == null ? null : new VariableTracker(mv, name, desc, className, access, localVariableNames);
    }
}
