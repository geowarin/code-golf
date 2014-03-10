package org.jetbrains.codeGolf.plugin.login;

/**
 * Date: 10/03/2014
 * Time: 21:09
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public class JBCredentials implements Credentials {
    private final String userName;
    private final String password;
    private final String token;

    JBCredentials(String userName, String password, String token) {
        this.password = password;
        this.userName = userName;
        this.token = token;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getUserName() {
        return userName;
    }
}
