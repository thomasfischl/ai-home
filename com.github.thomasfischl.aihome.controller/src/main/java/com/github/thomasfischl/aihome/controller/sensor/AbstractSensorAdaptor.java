package com.github.thomasfischl.aihome.controller.sensor;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;

public abstract class AbstractSensorAdaptor {

  private String name;

  private SensorDataType type;

  public AbstractSensorAdaptor(String name, SensorDataType type) {
    super();
    this.name = name;
    this.type = type;
  }

  protected SensorData createSensorData(String value) {
    return new SensorData(name, value, type, System.currentTimeMillis());
  }

  public abstract SensorData process();

}
