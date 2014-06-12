package com.github.thomasfischl.aihome.controller.sensor;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;
import com.github.thomasfischl.aihome.controller.pi.PiMicroController;

public class GPIOSensorAdaptor extends AbstractSensorAdaptor {

  private PiMicroController controller = PiMicroController.getInstance();

  private boolean state = false;

  public GPIOSensorAdaptor() {
    super("BTN", SensorDataType.BOOL);
  }

  @Override
  public SensorData process() {
    if (controller.isButtonPressed()) {
      state = !state;
    }
    return createSensorData(String.valueOf(state));
  }

}
