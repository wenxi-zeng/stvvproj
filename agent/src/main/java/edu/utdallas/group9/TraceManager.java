package edu.utdallas.group9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TraceManager {
    private static volatile TraceManager instance = null;

    private HashMap<String, TraceEntry> map;
    private String programName;
    private String currentTestcase;

    private TraceManager() {
        map = new HashMap<>();
    }

    public static TraceManager getInstance() {
        if (instance == null) {
            synchronized(CoverageManager.class) {
                if (instance == null) {
                    instance = new TraceManager();
                }
            }
        }

        return instance;
    }

    public void addDatum(String className, String methodName, String token, String var, String val, String type, boolean isField, boolean isDerived, int hashcode) {
        TraceEntry entry = map.get(token);
        if (entry == null) {
            entry = new TraceEntry()
                    .withClassName(className)
                    .withMethodName(methodName)
                    .withId(token)
                    .withTestCase(currentTestcase);
            map.put(token, entry);
        }

        TraceEntry.Datum datum = new TraceEntry.Datum()
                                .withDerived(isDerived)
                                .withHashcode(hashcode)
                                .withStatic(isField)
                                .withVarName(var)
                                .withVarValue(val)
                                .withVarType(type);

        entry.addDatum(datum);
    }

    public static void newDatum(String className, String methodName, String token, String var, String val, String type, boolean isField, boolean isDerived, int hashcode) {
        TraceManager.getInstance().addDatum(className, methodName, token, var, val, type, isField, isDerived, hashcode);
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
        return new ArrayList<>(map.values());
    }
}
