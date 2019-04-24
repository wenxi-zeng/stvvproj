package edu.utdallas.group9;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class VariableTracker extends MethodVisitor implements Opcodes {

    private String methodName;

    private String className;

    private String methodDescriptor;

    private int access;

    private String token;

    private Map<Integer, String> localVariableDict;

    private boolean beforeVisitCode;

    public VariableTracker(final MethodVisitor mv, String methodName, String methodDescriptor, String className, int access) {
        super(Opcodes.ASM5, mv);
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.className = className;
        this.access = access;
        this.localVariableDict = new HashMap<>();
        this.beforeVisitCode = true;
    }

    @Override
    public void visitCode() {
        beforeVisitCode = false;
        token = UUID.randomUUID().toString();
        int opcode;
        Integer hashcode = 0;
        String type;
        Type[] localVariableTypes = Type.getArgumentTypes(methodDescriptor);

        for (int i = 0; i < localVariableTypes.length; i++) {
            hashcode = 0;
            if (localVariableTypes[i].equals(Type.BOOLEAN_TYPE)) {
                opcode = ILOAD;
                type = "boolean";
            }
            else if (localVariableTypes[i].equals(Type.BYTE_TYPE)) {
                opcode = ILOAD;
                type = "byte";
            }
            else if (localVariableTypes[i].equals(Type.CHAR_TYPE)) {
                opcode = ILOAD;
                type = "char";
            }
            else if (localVariableTypes[i].equals(Type.SHORT_TYPE)) {
                opcode = ILOAD;
                type = "short";
            }
            else if (localVariableTypes[i].equals(Type.INT_TYPE)) {
                opcode = ILOAD;
                type = "int";
            }
            else if (localVariableTypes[i].equals(Type.LONG_TYPE)) {
                opcode = LLOAD;
                type = "long";
            }
            else if (localVariableTypes[i].equals(Type.FLOAT_TYPE)) {
                opcode = FLOAD;
                type = "float";
            }
            else if (localVariableTypes[i].equals(Type.DOUBLE_TYPE)) {
                opcode = DLOAD;
                type = "double";
            }
            else {
                opcode = ALOAD;
                type = "other";
                hashcode = localVariableTypes[i].getClassName().hashCode();
            }

            mv.visitLdcInsn(className);
            mv.visitLdcInsn(methodName);
            mv.visitLdcInsn(token);
            mv.visitLdcInsn(localVariableDict.get(i));

            mv.visitVarInsn(opcode, i);
            mv.visitMethodInsn(INVOKESTATIC, "Ljava/lang/String", "valueOf", localVariableTypes[i].getInternalName(), false);

            mv.visitLdcInsn(type);
            mv.visitLdcInsn(new Boolean(false));
            mv.visitLdcInsn(new Boolean(false));
            mv.visitLdcInsn(hashcode);

            mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/group9/TraceManager", "newDatum", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z;Z;I)V", false);
            // ith argument is now loaded on the operand stack.
            // add more ASM code to do what ever it is that you want to do with the argument.
        }

        super.visitCode();
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        if (beforeVisitCode) {
            localVariableDict.put(index, name);
        }

        super.visitLocalVariable(name, desc, signature, start, end, index);
    }
}
