package org.jetbrains.codeGolf.plugin.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.codeGolf.plugin.ExportableGolfSolution;
import org.jetbrains.codeGolf.plugin.GolfResult;
import org.jetbrains.codeGolf.plugin.GolfSolution;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.UserScore;

public class JsonSerializer
{
  public static String serializeTasks(List<GolfTask> tasks)
  {
    TasksData data = new TasksData();
    data.tasks.addAll(tasks);
    return serializeObject(data);
  }

  public static String serializeScores(List<UserScore> scores) {
    UserScoreData data = new UserScoreData();
    data.scores.addAll(scores);
    return serializeObject(data);
  }

  public static List<GolfTask> deserializeTasks(String json) {
    try {
      return ((TasksData)new ObjectMapper().readValue(json, TasksData.class)).tasks;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<UserScore> deserializeScores(String json) {
    try {
      return ((UserScoreData)new ObjectMapper().readValue(json, UserScoreData.class)).scores;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<GolfTask> deserializeTasks(InputStream json) {
    try {
      return ((TasksData)new ObjectMapper().readValue(json, TasksData.class)).tasks;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String serializeObject(Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static GolfResult deserializeSolutionJson(String json) {
    try {
      return (GolfResult)new ObjectMapper().readValue(json, GolfResult.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static GolfTask deserializeTask(InputStream input)
  {
    return (GolfTask)deserialize(input, GolfTask.class);
  }

  public static GolfSolution deserializeSolution(InputStream input) {
    return (GolfSolution)deserialize(input, GolfSolution.class);
  }

  private static <T> T deserialize(InputStream input, Class<T> type) {
    try {
      return new ObjectMapper().readValue(input, type);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String serializeSolutions(Collection<ExportableGolfSolution> solutions) {
    SolutionsData data = new SolutionsData();
    data.solutions.addAll(solutions);
    try {
      return new ObjectMapper().writeValueAsString(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<ExportableGolfSolution> deserializeSolutions(InputStream input) {
    try {
      SolutionsData data = (SolutionsData)new ObjectMapper().readValue(input, SolutionsData.class);
      return data.solutions;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static class SolutionsData
  {
    public List<ExportableGolfSolution> solutions = new ArrayList();
  }

  public static class UserScoreData
  {
    public List<UserScore> scores = new ArrayList();
  }

  public static class TasksData
  {
    public List<GolfTask> tasks = new ArrayList();
  }
}