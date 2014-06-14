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

  public List<Condition> getConditions() {
    return conditions;
  }

  public Condition getCondition(String name) {
    for (Condition c : conditions) {
      if (name.equals(c.getName())) {
        return c;
      }
    }
    return null;
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

  public void setName(String name) {
    this.name = name;
  }

  public Rule cloneRule() {
    Rule newRule = new Rule(name);
    for (Condition c : conditions) {
      newRule.addCondition(c.cloneCondition());
    }

    for (Result r : results) {
      newRule.addResult(r.cloneResult());
    }

    return newRule;
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
