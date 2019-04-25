package edu.utdallas.group9;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TraceClassTransformer implements ClassFileTransformer {

    private final String packageName;

    public TraceClassTransformer(String packageName) {
        this.packageName = packageName;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith(packageName)){
            org.objectweb.asm.ClassReader cr = new ClassReader(classfileBuffer);
            org.objectweb.asm.ClassWriter cw = new org.objectweb.asm.ClassWriter(ClassWriter.COMPUTE_FRAMES);
            TraceClassTransformVisitor ca = new TraceClassTransformVisitor(cw, className);
            cr.accept(ca, 0);
            System.out.println("================================From package");
            return cw.toByteArray();
        }

        //System.out.println("================================From outside. Class name: " + className +  ", package: " + packageName);
        return classfileBuffer;
    }
}