package edu.utdallas.group9;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassTransformer implements ClassFileTransformer {

    private final String packageName;

    public ClassTransformer(String packageName) {
        this.packageName = packageName.replaceAll("\\.", "/");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith(packageName)){
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassTransformVisitor ca = new ClassTransformVisitor(cw);
            cr.accept(ca, 0);
            //System.out.println("================================From package");
            return cw.toByteArray();
        }

        //System.out.println("================================From outside. Class name: " + className +  ", package: " + packageName);
        return classfileBuffer;
    }
}
