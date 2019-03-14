package edu.utdallas.group9;

import java.lang.Comparable;

public class ClassOutPut implements Comparable<ClassOutPut> {
    private String className;
    private int lineNumber;

    public ClassOutPut(String className, int lineNumber) {
        this.className = className;
        this.lineNumber = lineNumber;
    }

    public String getOutPutMsg() {
        return className + ":" + lineNumber + "\n";
    }

    @Override
    public int compareTo(ClassOutPut o) {
        return this.lineNumber - o.lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassOutPut)) {
            return false;
        }
        return this.className.equals(((ClassOutPut) o).className) && this. lineNumber == ((ClassOutPut) o).lineNumber;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(lineNumber).hashCode();
    }
}
