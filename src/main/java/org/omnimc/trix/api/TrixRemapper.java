/*
 * MIT License
 *
 * Copyright (c) 2026 OmniMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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