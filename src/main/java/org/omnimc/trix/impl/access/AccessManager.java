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

package org.omnimc.trix.impl.access;

import org.omnimc.trix.api.access.info.ClassAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public final class AccessManager {

    private static AccessManager INSTANCE;

    public static AccessManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AccessManager();
        }
        return INSTANCE;
    }

    private final Map<String, ClassAccess> classAccess = new HashMap<>();
    private final Map<String, ClassAccess> removedClassAccess = new HashMap<>();

    private AccessManager() {
    }

    public void addClassAccess(String className) {
        addClassAccess(className, new ClassAccess());
    }

    public void addClassAccess(String className, int access) {
        addClassAccess(className, new ClassAccess(access));
    }

    public void addClassAccess(String className, ClassAccess access) {
        if (removedClassAccess.containsKey(className)) {
            return;
        }

        if (classAccess.containsKey(className)) {
            ClassAccess originalAccess = classAccess.get(className);
            originalAccess.setAccess(access.getAccess());
            access = originalAccess;
        }

        classAccess.put(className, access);
    }

    public ClassAccess getClassAccess(String className) {
        return classAccess.get(className);
    }

    public void removeAccess(String className) {
        removedClassAccess.put(className, classAccess.get(className));
        classAccess.remove(className);
    }

    public void removeAccess(ClassAccess access) {
        classAccess.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == access)
                .forEach(entry -> {
                    classAccess.remove(entry.getKey());
                    removedClassAccess.put(entry.getKey(), entry.getValue());
                });
    }
}