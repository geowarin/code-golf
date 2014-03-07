package org.jetbrains.codeGolf.auth;

import com.jgoodies.common.base.Objects;

public final class AuthPackage {
    public static final boolean hasAdminAccess(String username) {
        return Objects.equals(username, "geowarin");
    }
}