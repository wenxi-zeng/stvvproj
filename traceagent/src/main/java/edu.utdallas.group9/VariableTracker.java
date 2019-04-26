package edu.utdallas.group9;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;
import java.util.UUID;

public class VariableTracker extends MethodVisitor implements Opcodes {

    private String methodName;

    private String className;

    private String rawClassName;

    private String methodDescriptor;

    private int access;

    private int counter;

    private boolean isStaticMethod;

    private String[] localVariableNames;

    private FieldNode[] fields;

    public VariableTracker(final MethodVisitor mv, String methodName, String methodDescriptor, String className, int access, String[] localVariableNames, List<FieldNode> fields) {
        super(Opcodes.ASM5, mv);
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.rawClassName = className;
        this.className = className.replaceAll("/", ".");
        this.access = access;
        this.counter = 0;
        this.isStaticMethod = (this.access & Opcodes.ACC_STATIC) != 0;
        this.localVariableNames = localVariableNames;

        int offset = isStaticMethod ? 0 : 1;
        int counter = 0;
        this.fields = new FieldNode[localVariableNames.length - offset];
        for (FieldNode node : fields) {
            if (isStaticMethod)
                this.fields[counter++] = node;
            else if (!node.name.equals("this$0"))
                this.fields[counter++] = node;
        }
    }

    @Override
    public void visitCode() {
        Type[] localVariableTypes = Type.getArgumentTypes(methodDescriptor);
        int offset = isStaticMethod ? 0 : 1;

        String varname;
        for (int i = 0; i < localVariableTypes.length; i++) {
            varname = localVariableNames != null ? localVariableNames[i + offset] : "varname";
            addLocalVariable(localVariableTypes[i].getDescriptor(), varname, i + offset);
        }
        super.visitCode();

//        for (FieldNode field : fields) {
//            //System.out.println("field.desc: " + field.desc + ", field.name" + field.name);
//            addEntry(field.access, field.desc, field.name, -1, true);
//        }
    }



    private void addLocalVariable(String desc, String varName, int index) {
        String token = UUID.randomUUID().toString();
        int opcode;
        int hashcode = 0;
        String repType;
        String internalType = desc;
        String strInternal = "L" + Type.getInternalName(String.class) + ";";
        String boolInternal = "I";
        String intInternal = "I";

        switch (desc) {
            case "Z":
                opcode = Opcodes.ILOAD;
                repType = "boolean";
                break;
            case "B":
                opcode = Opcodes.ILOAD;
                repType = "byte";
                break;
            case "C":
                opcode = Opcodes.ILOAD;
                repType = "char";
                break;
            case "S":
                opcode = Opcodes.ILOAD;
                repType = "short";
                break;
            case "I":
                opcode = Opcodes.ILOAD;
                repType = "int";
                break;
            case "J":
                opcode = Opcodes.LLOAD;
                repType = "long";
                break;
            case "F":
                opcode = Opcodes.FLOAD;
                repType = "float";
                break;
            case "D":
                opcode = Opcodes.DLOAD;
                repType = "double";
                break;
            default:
                opcode = Opcodes.ALOAD;
                repType = desc;
                hashcode = desc.hashCode();
                internalType = intInternal;
                break;
        }

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
        mv.visitLdcInsn(new Integer(00));
        mv.visitLdcInsn(new Integer(0));
        mv.visitLdcInsn(new Integer(hashcode));

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "edu/utdallas/group9/TraceManager", "newDatum", "(" + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + boolInternal
                                                                                                                + boolInternal
                                                                                                                + intInternal + ")V", false);

    }
}
