package org.jetbrains.codeGolf.auth;

public final class AuthResult {
    private final boolean isOk;
    private final String errorMessage;
    public static final object object$ = new object();

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

    public static final class object {
        private final AuthResult SUCCESS = new AuthResult((String) null);


        public final AuthResult getSUCCESS() {
            return this.SUCCESS;
        }
    }
}