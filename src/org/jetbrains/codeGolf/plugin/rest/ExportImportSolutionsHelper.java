package org.jetbrains.codeGolf.plugin.rest;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.codeGolf.auth.JBAccountAuthHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExportImportSolutionsHelper {

    public static void main(String[] args) throws IOException {
        File file = new File("tasks/solutions.json");
        if (args.length != 2) {
            printUsage();
            return;
        }
        String serverUrl = args[1];
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(args[0] + "ing solutions from " + serverUrl);
        System.out.println("Username:");
        String username = input.readLine();
        System.out.println("Password:");
        String password = input.readLine();
        String params = "?username=" + username + "&password=" + JBAccountAuthHelper.encodePassword(password);
        if (args[0].equals("import")) {
            String solutions = FileUtil.loadFile(file);
            String path = "/solution/import" + params;
            Pair result = RestClientUtil.postJsonToServer(solutions, serverUrl, path);
            System.out.println("Server " + result.getSecond() + " answered: " + result.getFirst());
        } else if (args[0].equals("export")) {
            RestClientUtil.loadJsonToFile(serverUrl, "/solution/export" + params, file);
            System.out.println("Solutions from " + serverUrl + " exported to " + file.getAbsolutePath());
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: import|export <server-url>");
    }
}