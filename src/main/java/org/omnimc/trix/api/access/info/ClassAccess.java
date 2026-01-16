package org.omnimc.trix.api.access.info;

import org.omnimc.trix.api.access.Access;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public final class ClassAccess extends Access {

    private final Map<String, MethodAccess> methods = new HashMap<>();
    private final Map<String, FieldAccess> fields = new HashMap<>();

    public ClassAccess() {
        super();
    }

    public ClassAccess(int access) {
        super(access);
    }

    public void addMethod(String name, String descriptor, int access) {
        methods.put(name + descriptor, new MethodAccess(access));
    }

    public MethodAccess getMethodAccess(String name, String descriptor) {
        return methods.get(name + descriptor);
    }

    public Integer getMethod(String name, String descriptor) {
        MethodAccess methodAccess = getMethodAccess(name, descriptor);
        if (methodAccess == null) {
            return DEFAULT_ACCESS;
        }

        return methodAccess.getAccess();
    }

    public void addField(String name, String descriptor, int access) {
        fields.put(name + descriptor, new FieldAccess(access));
    }

    public FieldAccess getFieldAccess(String name, String descriptor) {
        return fields.get(name + descriptor);
    }

    public Integer getField(String name, String descriptor) {
        FieldAccess fieldAccess = getFieldAccess(name, descriptor);
        if (fieldAccess == null) {
            return DEFAULT_ACCESS;
        }

        return fieldAccess.getAccess();
    }

    public Map<String, MethodAccess> getMethods() {
        return methods;
    }

    public Map<String, FieldAccess> getFields() {
        return fields;
    }
}