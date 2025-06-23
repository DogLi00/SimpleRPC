package com.upc.protocal;

public class Invocation {
    private String interfaceName;
    private String methodName;
    private Class[] paramTypes;
    private Object[] paramValues;

    public Invocation() {
    }

    public Invocation(String interfaceName, String methodName, Class[] paramTypes, Object[] paramValues) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.paramValues = paramValues;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public void setParamValues(Object[] paramValues) {
        this.paramValues = paramValues;
    }
}
