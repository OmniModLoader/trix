package org.omnimc.trix.api;

import org.objectweb.asm.commons.Remapper;
import org.omnimc.lumina.data.types.ClassData;
import org.omnimc.lumina.data.types.FieldData;
import org.omnimc.lumina.data.types.MethodData;
import org.omnimc.lumina.mcmap.McMap;
import org.omnimc.lumina.namespace.Namespace;

import java.io.IOException;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public class TrixRemapper extends Remapper {

    private final McMap chosenMap;

    public TrixRemapper(Namespace namespace) {
        this(namespace.getProperty());
    }

    public TrixRemapper(McMap chosenMap) {
        this.chosenMap = chosenMap;
    }

    @Override
    public String map(String internalName) {
        return mapType(internalName);
    }

    @Override
    public String mapType(String internalName) {
        ClassData aClass = getClass(internalName);
        if (aClass == null) {
            return internalName;
        }

        return aClass.getClassName();
    }

    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        ClassData classData = getClass(owner);
        if (classData == null) {
            return name;
        }

        FieldData field = classData.getField(name, descriptor != null ? mapDesc(descriptor) : "");

        return field.getFieldName();
    }

    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        ClassData classData = getClass(owner);
        if (classData == null) {
            return name;
        }

        MethodData method = classData.getMethod(name, descriptor != null ? mapMethodDesc(descriptor) : "");

        return method.getMethodName();
    }

    private ClassData getClass(String name) {
        try {
            return chosenMap.getClassData(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}