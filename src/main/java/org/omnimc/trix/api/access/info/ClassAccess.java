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