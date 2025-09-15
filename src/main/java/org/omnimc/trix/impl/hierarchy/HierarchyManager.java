package org.omnimc.trix.impl.hierarchy;

import org.omnimc.lumina.data.Mappings;
import org.omnimc.lumina.data.types.ClassData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HierarchyManager {

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