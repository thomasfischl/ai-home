package com.github.thomasfischl.aihome.controller.sensor;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;

public abstract class AbstractSensorAdaptor {

  private String name;

  private String type;

  private long schedulePeriod;

  protected String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  protected String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setSchedulePeriod(long schedulePeriod) {
    this.schedulePeriod = schedulePeriod;
  }

  protected long getSchedulePeriod() {
    return schedulePeriod;
  }

  public abstract SensorData process();

}
