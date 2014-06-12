package com.github.thomasfischl.aihome.controller.rule;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

public class Rule {

  private List<Condition> conditions = new ArrayList<>();

  private List<Result> results = new ArrayList<>();

  private String name;

  public Rule(String name) {
    super();
    this.name = name;
  }

  public void addCondition(Condition condition) {
    conditions.add(condition);
  }

  public void addResult(Result result) {
    results.add(result);
  }

  public List<Result> getResults() {
    return results;
  }

  public String getName() {
    return name;
  }

  public boolean evaluate(SensorDataGroup data) {
    boolean result = true;

    for (Condition condition : conditions) {
      result = result && condition.evaluate(data);
      // System.out.println("Result of condition '" + condition.getName() + "': " + result);
    }

    return result;
  }

}
