package edu.utdallas.group9;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.UUID;

public class VariableTracker extends MethodVisitor implements Opcodes {

    private String methodName;

    private String className;

    private String methodDescriptor;

    private int access;

    private int counter;

    private boolean isStaticMethod;

    public VariableTracker(final MethodVisitor mv, String methodName, String methodDescriptor, String className, int access) {
        super(Opcodes.ASM5, mv);
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.className = className;
        this.access = access;
        this.counter = 0;
        this.isStaticMethod = (this.access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    public void visitCode() {
        Type[] localVariableTypes = Type.getArgumentTypes(methodDescriptor);
        int offset = isStaticMethod ? 0 : 1;
        for (int i = 0; i < localVariableTypes.length; i++) {
            addEntry(localVariableTypes[i].getDescriptor(), "varname", i + offset);
        }
        super.visitCode();
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {


        //System.out.println("visitLocalVariable: index: " + index + ", name: " + name);
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }
    
    private void addEntry(String desc, String varName, int index) {
        Type varType = Type.getObjectType(desc);
        String token = UUID.randomUUID().toString();
        System.out.println("desc: " + desc + ", varType:" + varType.getInternalName());
        int opcode;
        int hashcode = 0;
        String repType;
        String internalType = varType.getInternalName();
        String strInternal = "L" + Type.getInternalName(String.class) + ";";
        String boolInternal = "I";
        String intInternal = "I";

        if (desc.equals("Z")) {
            opcode = Opcodes.ILOAD;
            repType = "boolean";
        }
        else if (desc.equals("B")) {
            opcode = Opcodes.ILOAD;
            repType = "byte";
        }
        else if (desc.equals("C")) {
            opcode = Opcodes.ILOAD;
            repType = "char";
        }
        else if (desc.equals("S")) {
            opcode = Opcodes.ILOAD;
            repType = "short";
        }
        else if (desc.equals("I")) {
            opcode = Opcodes.ILOAD;
            repType = "int";
        }
        else if (desc.equals("J")) {
            opcode = Opcodes.LLOAD;
            repType = "long";
        }
        else if (desc.equals("F")) {
            opcode = Opcodes.FLOAD;
            repType = "float";
        }
        else if (desc.equals("D")) {
            opcode = Opcodes.DLOAD;
            repType = "double";
        }
        else {
            opcode = Opcodes.ALOAD;
            repType = desc;
            hashcode = varType.getClassName().hashCode();
            internalType = intInternal;
        }

        System.out.println("loop invoked before pull dict");
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(token);
        mv.visitLdcInsn(varName);

        if (opcode == Opcodes.ALOAD) {
            String temp = "hashcode";
            mv.visitLdcInsn(temp);
        }
        else {
            mv.visitVarInsn(opcode, index);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, strInternal, "valueOf", "(" + internalType + ")" + strInternal, false);
        }

        mv.visitLdcInsn(repType);
        mv.visitLdcInsn(new Integer(0));
        mv.visitLdcInsn(new Integer(0));
        mv.visitLdcInsn(new Integer(hashcode));

        System.out.println("loop invoked");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "edu/utdallas/group9/TraceManager", "newDatum", "(" + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + boolInternal
                                                                                                                + boolInternal
                                                                                                                + intInternal + ")V", false);

        System.out.println("loop invoked 2");
    }
}
