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