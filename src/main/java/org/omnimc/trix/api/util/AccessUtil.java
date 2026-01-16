package org.omnimc.trix.api.util;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public final class AccessUtil {

    public static int mostOpenAccess(int original, int newAccess) {
        int result = 0;

        int visibility = pickMostOpenVisibility(original, newAccess);
        result |= visibility;

        int allFlagsExceptVisibility = (original | newAccess) & ~(ACC_PUBLIC | ACC_PROTECTED | ACC_PRIVATE);
        result |= allFlagsExceptVisibility;

        if (shouldKeepFinal(original, newAccess)) {
            result |= ACC_FINAL;
        } else {
            result &= ~ACC_FINAL;
        }

        return result;
    }

    private static int pickMostOpenVisibility(int a, int b) {
        int visA = a & (ACC_PUBLIC | ACC_PROTECTED | ACC_PRIVATE);
        int visB = b & (ACC_PUBLIC | ACC_PROTECTED | ACC_PRIVATE);

        if (visA == 0 && visB == 0) return 0;

        if (visA == ACC_PUBLIC || visB == ACC_PUBLIC) return ACC_PUBLIC;

        if (visA == ACC_PROTECTED || visB == ACC_PROTECTED) return ACC_PROTECTED;

        if (visA == 0 || visB == 0) return 0;

        return ACC_PRIVATE;
    }

    private static boolean shouldKeepFinal(int original, int newAccess) {
        boolean originalHadFinal = (original & ACC_FINAL) != 0;
        boolean newWantsFinal = (newAccess & ACC_FINAL) != 0;

        return originalHadFinal && newWantsFinal;
    }
}