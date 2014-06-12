package com.github.thomasfischl.aihome.controller.sensor;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;
import com.github.thomasfischl.aihome.controller.pi.PiMicroController;

public class GPIOSensorAdaptor extends AbstractSensorAdaptor {

  private PiMicroController controller = PiMicroController.getInstance();

  public GPIOSensorAdaptor() {
    super("Button", SensorDataType.BOOL);
  }

  @Override
  public SensorData process() {
    return createSensorData(Boolean.toString(controller.isButtonPressed()));
  }

}
