package com.github.thomasfischl.aihome.controller.rule;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

public class Condition {

  private String name;

  private String value;

  public Condition(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
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
    return data.getName().equals(name) && data.getValue().equals(value);
  }

}
