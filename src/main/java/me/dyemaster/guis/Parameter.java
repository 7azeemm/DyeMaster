package me.dyemaster.guis;

public class Parameter {
    Class<?> parameterTypes;
    Object parameters;

    public Class<?> getParameterTypes() {
        return parameterTypes;
    }

    public Object getParameters() {
        return parameters;
    }

    public Parameter(Class<?> parameterTypes, Object parameters) {
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }
}
