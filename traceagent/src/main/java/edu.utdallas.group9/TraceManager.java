package edu.utdallas.group9;

import java.util.ArrayList;
import java.util.List;

public class TraceManager {
    private static volatile TraceManager instance = null;

    private List<TraceEntry> traceEntries;
    private String programName;
    private String currentTestcase;

    private TraceManager() {
        traceEntries = new ArrayList<>();
    }

    public static TraceManager getInstance() {
        if (instance == null) {
            synchronized(TraceManager.class) {
                if (instance == null) {
                    instance = new TraceManager();
                }
            }
        }

        return instance;
    }

    public void addDatum(String className, String methodName, String token, String var, String val, String type, boolean isField, boolean isDerived, int hashcode) {
        TraceEntry entry = new TraceEntry()
                .withClassName(className)
                .withMethodName(methodName)
                .withToken(token)
                .withTestCase(currentTestcase)
                .withDerived(isDerived)
                .withHashcode(hashcode)
                .withParameter(!isField)
                .withVarName(var)
                .withVarValue(val)
                .withVarType(type);

        traceEntries.add(entry);
    }

    public static void newDatum(String className, String methodName, String token, String var, String val, String type, int isField, int isDerived, int hashcode) {
        //System.out.println("newDatum called");
        TraceManager.getInstance().addDatum(className, methodName, token, var, val, type, isField != 0, isDerived != 0, hashcode);
    }

    public void setProgramName(String name) {
        programName = name;
    }

    public String getProgramName() {
        return programName;
    }

    public void addCase(String caseName) {
        currentTestcase = caseName;
    }

    public List<TraceEntry> getTracedEntries (){
        return traceEntries;
    }
}
