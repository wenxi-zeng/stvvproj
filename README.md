# A Tool Set for Statement Coverage and Invariant Inference

## 1. Build Project

Build the project by using <code>mvn install</code>. Four artifacts shall be generated in their corresponding directiories:

* <code>Agent-1.0-SNAPSHOT.jar</code> under <code>agent\target</code>
* <code>AutoCoverageAgent-1.0-SNAPSHOT.jar</code> under <code>autocoverageagent\target</code>
* <code>TraceAgent-1.0-SNAPSHOT.jar</code> under <code>traceagent\target</code>
* <code>AutoTraceAgent-1.0-SNAPSHOT.jar</code> under <code>autotraceagent\target</code>

## 2. Statement Coverage

### 2.1 Agent

Agent is the driver that launches the statement coverage. To run it, manually updating the <code>pom.xml</code> with statement coverage JavaAgent and RunListener are required. Once the pom.xml is updated, use <code>mvn test</code> on the target project, the statement coverage shall be collected.

### 2.2 AutoCoverageAgent

AutoCoverageAgent hides the tedious <code>pom.xml</code>, thus simplifying the process. The AutoCoverageAgent automatically downloads the project from GitHub, updates <code>pom.xml</code>, invoke <code>mvn test</code> on the target program, and collects the coverage with a single command listed below. ***Note: the artifacts of Agent and AutoCoverageAgent are required to put into the same directory*** 

```bash
 java -jar AutoCoverageAgent-1.0-SNAPSHOT.jar <GitHub Repo of Target Project>
```

### 2.3 Results

The collected data can be found in <code>TargetProjectRoot/logs/</code> folder. For example: <code>commons-lang/logs</code>
 
## 3. Invariant Inference

### 3.1 Trace Agent

Similar to statement coverage agent, trace agent is the driver that launches the trace of internal state of variables. To run it, manually updating the <code>pom.xml</code> with trace JavaAgent and RunListener values are required. Once the pom.xml is updated, use <code>mvn test</code> on the target project, the trace data of variables shall be collected.

### 3.2 AutoTraceAgent

AutoTraceAgent hides the tedious <code>pom.xml</code>, thus simplifying the process. The AutoTraceAgent automatically downloads the project from GitHub, updates <code>pom.xml</code>, invoke <code>mvn test</code> on the target program, and collects variable trace with a single command listed below. ***Note: the artifacts of TraceAgent and AutoTraceAgent are required to put into the same directory***

```bash
 java -jar AutoTraceAgent-1.0-SNAPSHOT.jar <GitHub Repo of Target Project>
```

### 2.3 Results

The collected data can be found in <code>TargetProjectRoot/logs/</code> folder. For example: <code>commons-lang/logs</code>

### 2.4 Inferring Invariants

Once the trace data are collected, copy the Python code under backend folder (<code>backend/trace_infer.py</code>) to the folder where the trace data are located at (<code>TargetProjectRoot/logs/</code>), and enter the following command 

```bash
python trace_infer.py
```

The results of inferred invariants are written to <code>log</code> file under the same folder with <code>trace_infer.py</code>
