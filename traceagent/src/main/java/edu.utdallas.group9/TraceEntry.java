package edu.utdallas.group9;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TraceEntry implements Serializable
{

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("className")
    @Expose
    private String className;
    @SerializedName("methodName")
    @Expose
    private String methodName;
    @SerializedName("testCase")
    @Expose
    private String testCase;
    @SerializedName("varName")
    @Expose
    private String varName;
    @SerializedName("varValue")
    @Expose
    private String varValue;
    @SerializedName("varType")
    @Expose
    private String varType;
    @SerializedName("parameter")
    @Expose
    private boolean parameter;
    @SerializedName("derived")
    @Expose
    private boolean derived;
    @SerializedName("hashcode")
    @Expose
    private long hashcode;
    private final static long serialVersionUID = 4625025418890774682L;

    /**
     * No args constructor for use in serialization
     *
     */
    public TraceEntry() {
    }

    /**
     *
     * @param parameter
     * @param varName
     * @param token
     * @param varValue
     * @param derived
     * @param className
     * @param hashcode
     * @param testCase
     * @param varType
     * @param methodName
     */
    public TraceEntry(String token, String className, String methodName, String testCase, String varName, String varValue, String varType, boolean parameter, boolean derived, long hashcode) {
        super();
        this.token = token;
        this.className = className;
        this.methodName = methodName;
        this.testCase = testCase;
        this.varName = varName;
        this.varValue = varValue;
        this.varType = varType;
        this.parameter = parameter;
        this.derived = derived;
        this.hashcode = hashcode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TraceEntry withToken(String token) {
        this.token = token;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TraceEntry withClassName(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public TraceEntry withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public TraceEntry withTestCase(String testCase) {
        this.testCase = testCase;
        return this;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public TraceEntry withVarName(String varName) {
        this.varName = varName;
        return this;
    }

    public String getVarValue() {
        return varValue;
    }

    public void setVarValue(String varValue) {
        this.varValue = varValue;
    }

    public TraceEntry withVarValue(String varValue) {
        this.varValue = varValue;
        return this;
    }

    public String getVarType() {
        return varType;
    }

    public void setVarType(String varType) {
        this.varType = varType;
    }

    public TraceEntry withVarType(String varType) {
        this.varType = varType;
        return this;
    }

    public boolean isParameter() {
        return parameter;
    }

    public void setParameter(boolean parameter) {
        this.parameter = parameter;
    }

    public TraceEntry withParameter(boolean parameter) {
        this.parameter = parameter;
        return this;
    }

    public boolean isDerived() {
        return derived;
    }

    public void setDerived(boolean derived) {
        this.derived = derived;
    }

    public TraceEntry withDerived(boolean derived) {
        this.derived = derived;
        return this;
    }

    public long getHashcode() {
        return hashcode;
    }

    public void setHashcode(long hashcode) {
        this.hashcode = hashcode;
    }

    public TraceEntry withHashcode(long hashcode) {
        this.hashcode = hashcode;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("token", token).append("className", className).append("methodName", methodName).append("testCase", testCase).append("varName", varName).append("varValue", varValue).append("varType", varType).append("parameter", parameter).append("derived", derived).append("hashcode", hashcode).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(parameter).append(varName).append(token).append(varValue).append(derived).append(className).append(hashcode).append(testCase).append(varType).append(methodName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TraceEntry) == false) {
            return false;
        }
        TraceEntry rhs = ((TraceEntry) other);
        return new EqualsBuilder().append(parameter, rhs.parameter).append(varName, rhs.varName).append(token, rhs.token).append(varValue, rhs.varValue).append(derived, rhs.derived).append(className, rhs.className).append(hashcode, rhs.hashcode).append(testCase, rhs.testCase).append(varType, rhs.varType).append(methodName, rhs.methodName).isEquals();
    }

}