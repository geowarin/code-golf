package org.jetbrains.codeGolf.plugin.rest;

public class LoadTasksHelper {

    public static void main(String[] args) {
        RestClientUtil.loadTasks("http://localhost:3000");
    }
}