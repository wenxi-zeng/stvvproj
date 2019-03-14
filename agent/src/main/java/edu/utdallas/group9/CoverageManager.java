package edu.utdallas.group9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class CoverageManager {

    private static volatile CoverageManager instance = null;

    private HashMap<String, HashSet<ClassOutPut>> hm;
    private String programName;
    private HashSet<ClassOutPut> currentOutputs;

    private CoverageManager() {
        hm = new HashMap<>();
    }

    public static CoverageManager getInstance() {
        if (instance == null) {
            synchronized(CoverageManager.class) {
                if (instance == null) {
                    instance = new CoverageManager();
                }
            }
        }

        return instance;
    }

    public void addStatementCoverage(String className, int lineNumber) {
        currentOutputs.add(new ClassOutPut(className, lineNumber));
    }

    public static void newStatementCoverage(String className, int lineNumber) {
        CoverageManager.getInstance().addStatementCoverage(className, lineNumber);
    }

    public void setProgramName(String name) {
        programName = name;
    }

    public String getProgramName() {
        return programName;
    }

    public void addCase(String caseName) {
        hm.put(caseName, new HashSet<>());
        currentOutputs = hm.get(caseName);
    }

    public List<String> getCaseNames() {
        return new ArrayList<>(hm.keySet());
    }

    public HashSet<ClassOutPut> getOutputMsgs(String caseName) {
        return hm.get(caseName);
    }



}
