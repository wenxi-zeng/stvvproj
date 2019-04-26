package edu.utdallas.group9;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraceManager {
    private static volatile TraceManager instance = null;

    private final List<TraceEntry> traceEntries;
    private String programName;
    private String currentTestcase;
    private ExecutorService executor;
    private final Queue<List<TraceEntry>> queue;
    private int total;

    private TraceManager() {
        traceEntries = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(8);
        queue = new LinkedList<>();
        total = 0;
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
        synchronized (traceEntries) {
            if (traceEntries.size() > 1000) {
                total += traceEntries.size();
                queue.add(new ArrayList<>(traceEntries));
                traceEntries.clear();
                schedule();
            }
        }

        TraceEntry entry = new TraceEntry()
                .withClassName(className == null ? "null" : className)
                .withMethodName(methodName == null ? "null" : methodName)
                .withToken(token == null ? "null" : token)
                .withTestCase(currentTestcase == null ? "null" : currentTestcase)
                .withDerived(isDerived)
                .withHashcode(hashcode)
                .withParameter(!isField)
                .withVarName(var == null ? "null" : var)
                .withVarValue(val == null ? "null" : val)
                .withVarType(type == null ? "null" : type);

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

    public void complete() {
        synchronized (traceEntries) {
            total += traceEntries.size();
            queue.add(new ArrayList<>(traceEntries));
            traceEntries.clear();
        }

        flush();
        System.out.println("Total traced entries: " + total);
    }

    private void schedule() {
        executor.execute(this::flush);
    }

    private void flush() {
        while (true) {
            synchronized (queue) {
                if (queue.isEmpty()) break;
                List<TraceEntry> entries = queue.poll();
                if (entries == null || entries.size() == 0) continue;
                writeToFile(entries);
            }
        }
    }

    private void writeToFile(List<TraceEntry> entries) {
        String dir = "logs";
        String logPath = dir + File.separator + "trace" + UUID.randomUUID() + ".dat";
        try {
            File directory = new File(dir);
            if (! directory.exists()){
                directory.mkdir();
            }

            File file = new File(logPath);
            if (!file.exists())
                file.createNewFile();

//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            //System.out.println("Traced entries: " + entries.size());
//            objectMapper.writeValue(file, entries);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int i = 0;
            for (; i < entries.size() - 1; i++) {
                sb.append(entries.get(i).toString()).append(",");
            }
            sb.append(entries.get(i)).append("]");
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
