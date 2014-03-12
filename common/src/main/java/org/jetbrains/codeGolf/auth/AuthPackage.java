package org.jetbrains.codeGolf.auth;

public final class AuthPackage {
    public static boolean hasAdminAccess(String username) {
        return "geowarin".equals(username);
    }
}