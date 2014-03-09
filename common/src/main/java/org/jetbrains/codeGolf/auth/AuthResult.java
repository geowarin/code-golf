package org.jetbrains.codeGolf.auth;

public final class AuthResult {
    private final boolean isOk;
    private final String errorMessage;

    public static final AuthResult SUCCESS = new AuthResult(true);

    public final boolean getIsOk() {
        return this.isOk;
    }

    public final String getErrorMessage() {
        return this.errorMessage;
    }

    public AuthResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.isOk = false;
    }

    private AuthResult(boolean ok) {
        errorMessage = null;
        isOk = ok;
    }
}