package com.github.thomasfischl.aihome.communication.sensor;

public class SensorData {

  private String name;

  private String value;

  private String type;

  private long time;

  public SensorData() {
  }

  public SensorData(String name, String value, String type, long time) {
    super();
    this.name = name;
    this.value = value;
    this.type = type;
    this.time = time;
  }

  public String getName() {
    return name;
  }

  public long getTime() {
    return time;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "SensorData: [name:" + name + " value: " + value + " time: " + time + "]";
  }

}
