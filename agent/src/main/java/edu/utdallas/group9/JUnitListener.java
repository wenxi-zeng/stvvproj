package edu.utdallas.group9;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnitListener extends RunListener {

    private CoverageManager manager;

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        // read the singleton instance into global variable
        manager = CoverageManager.getInstance();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        String dir = "logs";
        String logPath = dir + File.separator + manager.getProgramName() + ".txt";
        ClassOutPut[] classOutputs;
        try {
            File directory = new File(dir);
            if (! directory.exists()){
                directory.mkdir();
            }

            File file = new File(logPath);
            if (!file.exists())
                file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            StringBuilder sb = new StringBuilder();
            for (String caseName : manager.getCaseNames()) {
                sb.append(caseName + "\n");
                classOutputs = manager.getOutputMsgs(caseName).toArray(new ClassOutPut[0]);
                Arrays.sort(classOutputs);
                for (ClassOutPut c : classOutputs) {
                    sb.append(c.getOutPutMsg());
                }
            }
            bw.write(sb.toString());
            bw.close();


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

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);

    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        super.testAssumptionFailure(failure);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
    }
}
