package edu.utdallas.group9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public final class CoverageManager {

    private static volatile CoverageManager instance = null;

    private HashMap<String, List<String>> hm;
    private String programName;
    private List<String> currentOutputs;

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
        currentOutputs.add(className + ":" + Integer.toString(lineNumber) + "\n");
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
        currentOutputs = new LinkedList<>();
        hm.put(caseName, currentOutputs);
    }

    public List<String> getCaseNames() {
        return new ArrayList<>(hm.keySet());
    }

    public List<String> getOutputMsgs(String caseName) {
        return hm.get(caseName);
    }



}
