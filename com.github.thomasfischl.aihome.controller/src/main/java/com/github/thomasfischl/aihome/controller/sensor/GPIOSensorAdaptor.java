package com.github.thomasfischl.aihome.controller.sensor;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;

public class GPIOSensorAdaptor extends AbstractSensorAdaptor {

  @Override
  public SensorData process() {
    return new SensorData(getName(), "" + System.currentTimeMillis(), getType(), System.currentTimeMillis());
  }

}
