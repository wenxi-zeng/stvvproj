package edu.utdallas.group9;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.HashSet;

class ClassTransformVisitor extends ClassVisitor implements Opcodes {

    private HashMap<String, MethodTransformVisitor> map = new HashMap<>();

    public ClassTransformVisitor(final ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null) return null;
        if (map.containsKey(name)) {
            MethodTransformVisitor transformer = map.get(name);
            HashSet<Integer> visitedLines = new HashSet<>(transformer.getVisitedLines());
            MethodTransformVisitor newTransformer = new MethodTransformVisitor(mv, name);
            newTransformer.setVisitedLines(visitedLines);
            map.put(name, newTransformer);
            return newTransformer;
        }
        else {
            MethodTransformVisitor transformer = new MethodTransformVisitor(mv, name);
            map.put(name, transformer);
            return transformer;
        }
    }
}

