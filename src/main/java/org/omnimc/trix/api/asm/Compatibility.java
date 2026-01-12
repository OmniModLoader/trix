package org.omnimc.trix.api.asm;

import org.objectweb.asm.Opcodes;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public final class Compatibility {

    public static int ASM_VERSION = Opcodes.ASM9;

    public static void setAsmVersion(int asmVersion) {
        ASM_VERSION = asmVersion;
    }

}