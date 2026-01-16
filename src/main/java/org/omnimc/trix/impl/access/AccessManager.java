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