package com.github.thomasfischl.aihome.controller.rule;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;

public class Result {
  private String name;

  private String value;

  public Result(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public SensorData getSensorData() {
    return new SensorData(name, value, SensorDataType.BOOL);
  }

  public Result cloneResult() {
    return new Result(name, value);
  }

}
