package org.omnimc.trix.api.access;

import org.omnimc.trix.api.util.AccessUtil;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public class Access {

    public static final int DEFAULT_ACCESS = -1;

    private int access;

    public Access() {
        this(DEFAULT_ACCESS);
    }

    public Access(int access) {
        this.access = access;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = AccessUtil.mostOpenAccess(this.access, access);
    }
}