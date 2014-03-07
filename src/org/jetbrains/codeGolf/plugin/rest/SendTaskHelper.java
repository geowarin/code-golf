package org.jetbrains.codeGolf.plugin.rest;

import org.jetbrains.codeGolf.plugin.GolfSolution;

public class SendTaskHelper
{
  public static void main(String[] args)
  {
    RestClientUtil.sendSolution(new GolfSolution("0-0", "User", 1, 2, 1, ""), "http://localhost:3000", "123");
  }
}