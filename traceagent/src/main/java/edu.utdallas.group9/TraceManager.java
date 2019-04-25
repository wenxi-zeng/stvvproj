package edu.utdallas.group9;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraceManager {
    private static volatile TraceManager instance = null;

    private final List<TraceEntry> traceEntries;
    private String programName;
    private String currentTestcase;
    private ExecutorService executor;
    private Queue<List<TraceEntry>> queue;
    private int counter;

    private TraceManager() {
        traceEntries = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(8);
        queue = new LinkedList<>();
        counter = 0;
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
        if (traceEntries.size() > 1000) save();

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

    public synchronized void save() {
        counter++;
        queue.add(new ArrayList<>(traceEntries));
        traceEntries.clear();
        executor.execute(this::flush);
    }

    private void flush() {
        while (!queue.isEmpty()) {
            List<TraceEntry> entries = queue.poll();
            writeToFile(entries);
        }
    }

    private void writeToFile(List<TraceEntry> entries) {
        String dir = "logs";
        String logPath = dir + File.separator + "trace" + counter + ".dat";
        try {
            File directory = new File(dir);
            if (! directory.exists()){
                directory.mkdir();
            }

            File file = new File(logPath);
            if (!file.exists())
                file.createNewFile();

            Writer writer = new FileWriter(logPath);
            Gson gson = new GsonBuilder().create();
            //System.out.println("Traced entries: " + entries.size());
            gson.toJson(entries, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
