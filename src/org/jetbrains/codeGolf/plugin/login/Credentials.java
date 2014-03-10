package org.jetbrains.codeGolf.plugin.login;

/**
 * Date: 10/03/2014
 * Time: 21:13
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public interface Credentials {
    String getPassword();

    String getToken();

    String getUserName();

    public static Credentials ANONYMOUS = new Credentials() {
        @Override
        public String getPassword() {
            return "";
        }

        @Override
        public String getToken() {
            return "";
        }

        @Override
        public String getUserName() {
            return "";
        }
    };
}
