package edu.utdallas.group9;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;
import java.util.UUID;

public class VariableTracker extends MethodVisitor implements Opcodes {

    private String methodName;

    private String methodSignature;

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

        if (fields != null) {
            this.fields = new FieldNode[fields.size()];
            this.fields = fields.toArray(this.fields);
        }
        StringBuilder parameters = new StringBuilder().append(methodName).append("(");
        for (int i = 0; i < fields.size(); i++) {
            this.fields[i] = fields.get(i);
            parameters.append(this.fields[i].desc).append(" ");
        }
        parameters.append(")");
        this.methodSignature =  parameters.toString();
    }

    @Override
    public void visitCode() {
        if (fields != null) {
            for (FieldNode field : fields) {
                addField(field);
            }
        }

        Type[] localVariableTypes = Type.getArgumentTypes(methodDescriptor);
        int offset = isStaticMethod ? 0 : 1;

        String varname;
        for (int i = 0; i < localVariableTypes.length; i++) {
            varname = localVariableNames != null ? localVariableNames[i + offset] : "varname";
            addLocalVariable(localVariableTypes[i].getDescriptor(), varname, i + offset);
        }

        super.visitCode();
    }

    private void addField(FieldNode fieldNode) {
        if (isStaticMethod || methodName.equals("<init>")) return;
        String token = UUID.randomUUID().toString();
        int opcode;
        int hashcode = 0;
        String repType;
        String toStringType = fieldNode.desc;
        String strInternal = "L" + Type.getInternalName(String.class) + ";";
        String boolInternal = "I";
        String intInternal = "I";
        String fieldMethodName = "field";
        //String charInternal = "C";

        switch (fieldNode.desc) {
            case "Z":
                opcode = Opcodes.ILOAD;
                repType = "boolean";
                break;
            case "B":
                opcode = Opcodes.ILOAD;
                repType = "byte";
                toStringType = intInternal;
                break;
            case "C":
                opcode = Opcodes.ILOAD;
                repType = "char";
                break;
            case "S":
                opcode = Opcodes.ILOAD;
                repType = "short";
                toStringType = intInternal;
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
//                opcode = Opcodes.ALOAD;
//                repType = fieldNode.desc;
//                hashcode = fieldNode.desc.hashCode();
//                internalType = intInternal;
//                break;
                return;
        }

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(fieldMethodName);
        mv.visitLdcInsn(token);
        mv.visitLdcInsn(fieldNode.name);

        if (opcode == Opcodes.ALOAD) {
            String temp = "hashcode";
            mv.visitLdcInsn(temp);
        }
        else {
            boolean isStaticField = (fieldNode.access & Opcodes.ACC_STATIC) != 0;
            if (isStaticField) {
                mv.visitFieldInsn(Opcodes.GETSTATIC, rawClassName, fieldNode.name, fieldNode.desc);
            }
            else {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                //System.out.println("mv.visitFieldInsn(Opcodes.GETFIELD, " + rawClassName + " , " + fieldNode.name + ", " + fieldNode.desc + ") method: " + methodName + ", value: " + fieldNode.value);
                mv.visitFieldInsn(Opcodes.GETFIELD, rawClassName, fieldNode.name, fieldNode.desc);
            }
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, strInternal, "valueOf", "(" + toStringType + ")" + strInternal, false);
        }

        mv.visitLdcInsn(repType);
        mv.visitLdcInsn(new Integer(1));
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

    private void addLocalVariable(String desc, String varName, int index) {
        String token = UUID.randomUUID().toString();
        int opcode;
        int hashcode = 0;
        String repType;
        String toStringType = desc;
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
                toStringType = intInternal;
                break;
            case "C":
                opcode = Opcodes.ILOAD;
                repType = "char";
                break;
            case "S":
                opcode = Opcodes.ILOAD;
                repType = "short";
                toStringType = intInternal;
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
//                opcode = Opcodes.ALOAD;
//                repType = desc;
//                hashcode = desc.hashCode();
//                internalType = intInternal;
//                break;

                return;
        }

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodSignature);
        mv.visitLdcInsn(token);
        mv.visitLdcInsn(varName);

        if (opcode == Opcodes.ALOAD) {
            String temp = "hashcode";
            mv.visitLdcInsn(temp);
        }
        else {
            mv.visitVarInsn(opcode, index);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, strInternal, "valueOf", "(" + toStringType + ")" + strInternal, false);
        }

        mv.visitLdcInsn(repType);
        mv.visitLdcInsn(new Integer(0));
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
