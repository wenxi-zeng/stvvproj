package edu.utdallas.group9;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class TraceRunListener extends RunListener {

    private TraceManager manager;

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        // read the singleton instance into global variable
        manager = TraceManager.getInstance();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        String dir = "logs";
        String logPath = dir + File.separator + "trace.dat";
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
            System.out.println("Traced entries: " + manager.getTracedEntries().size());
            Thread.sleep(1000);
            gson.toJson(manager.getTracedEntries(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        String caseName = "[TEST] " + description.getClassName() + ":" + description.getMethodName();
        manager.addCase(caseName);
    }
}

