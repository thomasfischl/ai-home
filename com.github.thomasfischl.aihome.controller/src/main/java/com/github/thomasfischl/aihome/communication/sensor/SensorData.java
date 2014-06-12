package com.github.thomasfischl.aihome.communication.sensor;

public class SensorData {

  private String name;

  private String value;

  private SensorDataType type;

  public SensorData() {
  }

  public SensorData(String name, String value, SensorDataType type) {
    super();
    this.name = name;
    this.value = value;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public SensorDataType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "SensorData: [name:" + name + " value: " + value + "]";
  }

}
