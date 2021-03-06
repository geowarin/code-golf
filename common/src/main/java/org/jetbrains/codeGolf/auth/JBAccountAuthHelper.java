package org.jetbrains.codeGolf.auth;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

public class JBAccountAuthHelper {
    public static AuthResult login(String username, String password) {
        return loginUsingEncodedPassword(username, encodePassword(password));
    }

    public static AuthResult loginUsingEncodedPassword(String username, String encodedPassword) {
        String serverUrl = "http://configr.jetbrains.com";
        Client client = Client.create();
        try {
            WebResource webResource = client.resource(serverUrl + "/login?user=" + username + "&scrumbled-password=" + encodedPassword);
            ClientResponse response = webResource.type(MediaType.TEXT_PLAIN_TYPE).post(ClientResponse.class);
            int status = response.getStatus();
            String output = response.getEntity(String.class);
            if (status == 200) {
                return AuthResult.SUCCESS;
            }
            return new AuthResult(output);
        } catch (Exception e) {
            return new AuthResult(e.getMessage() != null ? e.getMessage() : "error");
        } finally {
            client.destroy();
        }
    }

    public static void main(String[] args) {
        login("nik", "123");
    }

    public static String encodePassword(String password) {
        String result = "";
        if (password == null) return result;
        for (int i = 0; i < password.length(); i++) {
            int c = password.charAt(i);
            c ^= 57258;
            result = result + Integer.toHexString(c);
        }
        return result;
    }
}