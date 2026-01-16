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

package org.omnimc.trix.impl.hierarchy;

import org.omnimc.lumina.data.Mappings;
import org.omnimc.lumina.data.types.ClassData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class HierarchyManager {

    private final Mappings mappings = new Mappings();

    public Mappings getMappings() {
        return mappings;
    }

    public void addClass(String obfuscatedName, ClassData data) {
        mappings.addClass(obfuscatedName, data);
    }
    
    public void populateClassFiles() {
        final HashMap<String, ClassData> classFileHashMap = new HashMap<>();

        for (Map.Entry<String, ClassData> entry : mappings.getClasses().entrySet()) {
            String className = entry.getKey();
            ClassData originalClassFile = entry.getValue();

            ArrayList<String> dependencies = new ArrayList<>(originalClassFile.getDependentClasses());
            while (!dependencies.isEmpty()) {
                ArrayList<String> nextDependencies = new ArrayList<>();
                for (String dependency : dependencies) {
                    ClassData file = mappings.getClass(dependency);
                    if (file != null) {
                        originalClassFile.getFields().putAll(file.getFields());
                        originalClassFile.getMethods().putAll(file.getMethods());
                        nextDependencies.addAll(file.getDependentClasses());
                    }
                }
                dependencies = nextDependencies;
            }

            classFileHashMap.put(className, originalClassFile);
        }

        mappings.getClasses().clear();
        mappings.getClasses().putAll(classFileHashMap);
    }
}