package com.github.thomasfischl.aihome.controller.rule;

import java.util.HashSet;
import java.util.Set;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;

public class Condition {

  private String name;

  private Set<String> values = new HashSet<>();

  private SensorDataType type;

  public Condition(String name, String value) {
    this.name = name;
    addValue(value);
  }

  public Condition(String name, String value, SensorDataType type) {
    this.name = name;
    this.type = type;
    addValue(value);
  }

  public Condition(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void addValue(String value) {
    values.add(value);
  }

  public Set<String> getValues() {
    return values;
  }

  public SensorDataType getType() {
    return type;
  }

  public boolean evaluate(SensorDataGroup data) {
    for (SensorData d : data.getValues()) {
      if (evaluate(d)) {
        return true;
      }
    }
    return false;
  }

  public boolean evaluate(SensorData data) {
    return data.getName().equals(name) && values.contains(data.getValue());
  }

  public Condition cloneCondition() {
    Condition newCondition = new Condition(name);
    for (String val : values) {
      newCondition.addValue(val);
    }
    return newCondition;
  }

}
