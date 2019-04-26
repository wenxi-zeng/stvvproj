package edu.utdallas.group9;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "token",
        "className",
        "methodName",
        "testCase",
        "varName",
        "varValue",
        "varType",
        "parameter",
        "derived",
        "hashcode"
})
public class TraceEntry implements Serializable
{

    @JsonProperty("token")
    private String token;
    @JsonProperty("className")
    private String className;
    @JsonProperty("methodName")
    private String methodName;
    @JsonProperty("testCase")
    private String testCase;
    @JsonProperty("varName")
    private String varName;
    @JsonProperty("varValue")
    private String varValue;
    @JsonProperty("varType")
    private String varType;
    @JsonProperty("parameter")
    private boolean parameter;
    @JsonProperty("derived")
    private boolean derived;
    @JsonProperty("hashcode")
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

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    public TraceEntry withToken(String token) {
        this.token = token;
        return this;
    }

    @JsonProperty("className")
    public String getClassName() {
        return className;
    }

    @JsonProperty("className")
    public void setClassName(String className) {
        this.className = className;
    }

    public TraceEntry withClassName(String className) {
        this.className = className;
        return this;
    }

    @JsonProperty("methodName")
    public String getMethodName() {
        return methodName;
    }

    @JsonProperty("methodName")
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public TraceEntry withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @JsonProperty("testCase")
    public String getTestCase() {
        return testCase;
    }

    @JsonProperty("testCase")
    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public TraceEntry withTestCase(String testCase) {
        this.testCase = testCase;
        return this;
    }

    @JsonProperty("varName")
    public String getVarName() {
        return varName;
    }

    @JsonProperty("varName")
    public void setVarName(String varName) {
        this.varName = varName;
    }

    public TraceEntry withVarName(String varName) {
        this.varName = varName;
        return this;
    }

    @JsonProperty("varValue")
    public String getVarValue() {
        return varValue;
    }

    @JsonProperty("varValue")
    public void setVarValue(String varValue) {
        this.varValue = varValue;
    }

    public TraceEntry withVarValue(String varValue) {
        this.varValue = varValue;
        return this;
    }

    @JsonProperty("varType")
    public String getVarType() {
        return varType;
    }

    @JsonProperty("varType")
    public void setVarType(String varType) {
        this.varType = varType;
    }

    public TraceEntry withVarType(String varType) {
        this.varType = varType;
        return this;
    }

    @JsonProperty("parameter")
    public boolean isParameter() {
        return parameter;
    }

    @JsonProperty("parameter")
    public void setParameter(boolean parameter) {
        this.parameter = parameter;
    }

    public TraceEntry withParameter(boolean parameter) {
        this.parameter = parameter;
        return this;
    }

    @JsonProperty("derived")
    public boolean isDerived() {
        return derived;
    }

    @JsonProperty("derived")
    public void setDerived(boolean derived) {
        this.derived = derived;
    }

    public TraceEntry withDerived(boolean derived) {
        this.derived = derived;
        return this;
    }

    @JsonProperty("hashcode")
    public long getHashcode() {
        return hashcode;
    }

    @JsonProperty("hashcode")
    public void setHashcode(long hashcode) {
        this.hashcode = hashcode;
    }

    public TraceEntry withHashcode(long hashcode) {
        this.hashcode = hashcode;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{").append("\"token\":\"").append(token.isEmpty() ? " " : token).append("\",")
                                                .append("\"className\":\"").append(className.isEmpty() ? " " : className).append("\",")
                                                .append("\"methodName\":\"").append(methodName.isEmpty() ? " " : methodName).append("\",")
                                                .append("\"testCase\":\"").append(testCase.isEmpty() ? " " : testCase).append("\",")
                                                .append("\"varName\":\"").append(varName.isEmpty() ? " " : varName).append("\",")
                                                .append("\"varValue\":\"").append(varValue.isEmpty() ? " " : varValue).append("\",")
                                                .append("\"varType\":\"").append(varType.isEmpty() ? " " : varType).append("\",")
                                                .append("\"parameter\":").append(parameter).append(",")
                                                .append("\"derived\":").append(derived).append(",")
                                                .append("\"hashcode\":").append(hashcode).append("}").toString();
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