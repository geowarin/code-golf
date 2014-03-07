package org.jetbrains.codeGolf.plugin.rest;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.codeGolf.plugin.*;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RestClientUtil {

    private static final Logger LOG = Logger.getInstance(RestClientUtil.class.getName());

    public static List<GolfTask> loadTasks(String serverUrl) {
        Client client = Client.create();
        try {
            WebResource webResource = client.resource(getActualServerUrl(serverUrl, client) + "/task/list.json");

            ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
            String output;
            try {
                LOG.info("Loading tasks from server...");
                output = response.getEntity(String.class);
            } finally {
            }
            LOG.info("Loaded from server:");
            LOG.info(output);
            List tasks = JsonSerializer.deserializeTasks(output);
            LOG.info(String.valueOf(tasks.size()) + " tasks loaded");
            return tasks;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            client.destroy();
        }
    }

    public static List<UserScore> loadScores(String serverUrl, String username) {
        Client client = Client.create();
        try {
            WebResource webResource = client.resource(getActualServerUrl(serverUrl, client) + "/results.json/" + username);

            ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
            String output;
            try {
                LOG.info("Loading scores from server...");
                output = response.getEntity(String.class);
            } finally {
            }
            LOG.info("Loaded from server:");
            LOG.info(output);
            List scores = JsonSerializer.deserializeScores(output);
            LOG.info(String.valueOf(scores.size()) + " scores loaded");
            return scores;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            client.destroy();
        }
    }

    private static String getActualServerUrl(String serverUrl, Client client) {
        WebResource webResource = client.resource(serverUrl + "/redirect.json");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            LOG.info("Response status for " + serverUrl + " is " + response.getStatus() + ", not redirected");
            return serverUrl;
        }
        String output = response.getEntity(String.class);
        try {
            JsonNode node = new ObjectMapper().readTree(output);
            JsonNode noRedirect = node.get("no-redirect");
            if ((noRedirect != null) && (noRedirect.asBoolean())) {
                return serverUrl;
            }
            String server = node.get("server").asText();
            LOG.info("Redirecting to " + server);
            return server;
        } catch (Exception e) {
            LOG.info("Cannot parse '" + output + "', not redirected");
        }
        return serverUrl;
    }

    public static String sendNewTask(GolfTask task, String password) {
        return sendNewTask(task, getServerUrl(), password);
    }

    public static String sendNewTask(GolfTask task, String serverUrl, String password) {
        Pair res = postJsonToServer(task, serverUrl, "/task/new?password=" + password);
        String taskUrl = (String) res.getFirst();
        if (taskUrl.startsWith("Error:")) return taskUrl;
        return res.getSecond() + taskUrl;
    }

    public static GolfResult sendSolution(GolfSolution solution, String encodedPassword) {
        return sendSolution(solution, getServerUrl(), encodedPassword);
    }

    public static GolfResult sendSolution(GolfSolution solution, String serverUrl, String encodedPassword) {
        Pair res = postJsonToServer(solution, serverUrl, "/solution/new" + (encodedPassword != null ? "?password=" + encodedPassword : ""));
        String output = (String) res.getFirst();
        GolfResult golfResult = JsonSerializer.deserializeSolutionJson(output);
        String url = res.getSecond() + golfResult.getUrl();
        golfResult.setUrl(url);
        return golfResult;
    }

    private static String getServerUrl() {
        return CodeGolfSettings.getServerUrl();
    }

    public static Pair<String, String> postJsonToServer(Object data, String serverUrl, String path) {
        try {
            return postJsonToServer(new ObjectMapper().writeValueAsString(data), serverUrl, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pair<String, String> postJsonToServer(String json, String serverUrl, String path) {
        Client client = Client.create();
        try {
            String actualServerUrl = getActualServerUrl(serverUrl, client);
            String url = actualServerUrl + path;
            WebResource webResource = client.resource(url);
            LOG.info("Sending object to server " + url + ": " + json);
            ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, json);
            String output = response.getEntity(String.class);
            LOG.info("Server answered: " + output);
            return Pair.create(output, actualServerUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            client.destroy();
        }
    }

    public static void loadJsonToFile(String serverUrl, String path, File target) {
        Client client = Client.create();
        try {
            WebResource webResource = client.resource(getActualServerUrl(serverUrl, client) + path);
            ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
            FileOutputStream output;
            try {
                InputStream input = response.getEntityInputStream();
                output = new FileOutputStream(target);
                FileUtil.copy(input, output);
            } finally {
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            client.destroy();
        }
    }
}