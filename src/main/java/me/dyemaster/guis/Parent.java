package me.dyemaster.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parent {
    Class parentClass;
    List<Parameter> parameters = new ArrayList<>();

    public Class getParentClass() {
        return parentClass;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Parent(Class parent, Parameter... parameters) {
        this.parentClass = parent;
        this.parameters.addAll(List.of(parameters));
    }

    public Parent(Class parent) {
        this.parentClass = parent;
    }

    public Map<Class<?>, Object> toHashmap() {
        Map<Class<?>, Object> hashmap = new HashMap<>();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                hashmap.put(parameter.getParameterTypes(), parameter.getParameters());
            }
        }
        return hashmap;
    }
}
