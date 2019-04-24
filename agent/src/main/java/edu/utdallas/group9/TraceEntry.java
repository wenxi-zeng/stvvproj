package edu.utdallas.group9;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TraceEntry implements Serializable
{

    @SerializedName("className")
    @Expose
    private String className;
    @SerializedName("methodName")
    @Expose
    private String methodName;
    @SerializedName("testCase")
    @Expose
    private String testCase;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    private final static long serialVersionUID = -3305597895533626589L;

    /**
     * No args constructor for use in serialization
     *
     */
    public TraceEntry() {
    }

    /**
     *
     * @param id
     * @param data
     * @param className
     * @param testCase
     * @param methodName
     */
    public TraceEntry(String className, String methodName, String testCase, String id, List<Datum> data) {
        super();
        this.className = className;
        this.methodName = methodName;
        this.testCase = testCase;
        this.id = id;
        this.data = data;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TraceEntry withId(String id) {
        this.id = id;
        return this;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public TraceEntry withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    public void addDatum(Datum datum) {
        if (data == null) {
            data = new ArrayList<>();
        }

        data.add(datum);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("className", className).append("methodName", methodName).append("testCase", testCase).append("id", id).append("data", data).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(data).append(className).append(testCase).append(methodName).toHashCode();
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
        return new EqualsBuilder().append(id, rhs.id).append(data, rhs.data).append(className, rhs.className).append(testCase, rhs.testCase).append(methodName, rhs.methodName).isEquals();
    }

    public static class Datum implements Serializable
    {

        @SerializedName("varName")
        @Expose
        private String varName;
        @SerializedName("varValue")
        @Expose
        private String varValue;
        @SerializedName("varType")
        @Expose
        private String varType;
        @SerializedName("static")
        @Expose
        private boolean _static;
        @SerializedName("derived")
        @Expose
        private boolean derived;
        @SerializedName("hashcode")
        @Expose
        private long hashcode;
        private final static long serialVersionUID = -3270322593285331207L;

        /**
         * No args constructor for use in serialization
         *
         */
        public Datum() {
        }

        /**
         *
         * @param varName
         * @param _static
         * @param varValue
         * @param derived
         * @param hashcode
         * @param varType
         */
        public Datum(String varName, String varValue, String varType, boolean _static, boolean derived, long hashcode) {
            super();
            this.varName = varName;
            this.varValue = varValue;
            this.varType = varType;
            this._static = _static;
            this.derived = derived;
            this.hashcode = hashcode;
        }

        public String getVarName() {
            return varName;
        }

        public void setVarName(String varName) {
            this.varName = varName;
        }

        public Datum withVarName(String varName) {
            this.varName = varName;
            return this;
        }

        public String getVarValue() {
            return varValue;
        }

        public void setVarValue(String varValue) {
            this.varValue = varValue;
        }

        public Datum withVarValue(String varValue) {
            this.varValue = varValue;
            return this;
        }

        public String getVarType() {
            return varType;
        }

        public void setVarType(String varType) {
            this.varType = varType;
        }

        public Datum withVarType(String varType) {
            this.varType = varType;
            return this;
        }

        public boolean isStatic() {
            return _static;
        }

        public void setStatic(boolean _static) {
            this._static = _static;
        }

        public Datum withStatic(boolean _static) {
            this._static = _static;
            return this;
        }

        public boolean isDerived() {
            return derived;
        }

        public void setDerived(boolean derived) {
            this.derived = derived;
        }

        public Datum withDerived(boolean derived) {
            this.derived = derived;
            return this;
        }

        public long getHashcode() {
            return hashcode;
        }

        public void setHashcode(long hashcode) {
            this.hashcode = hashcode;
        }

        public Datum withHashcode(long hashcode) {
            this.hashcode = hashcode;
            return this;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("varName", varName).append("varValue", varValue).append("varType", varType).append("_static", _static).append("derived", derived).append("hashcode", hashcode).toString();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(varName).append(_static).append(varValue).append(derived).append(hashcode).append(varType).toHashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if ((other instanceof Datum) == false) {
                return false;
            }
            Datum rhs = ((Datum) other);
            return new EqualsBuilder().append(varName, rhs.varName).append(_static, rhs._static).append(varValue, rhs.varValue).append(derived, rhs.derived).append(hashcode, rhs.hashcode).append(varType, rhs.varType).isEquals();
        }

    }
}