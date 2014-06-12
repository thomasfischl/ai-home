package com.github.thomasfischl.aihome.controller.actors;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.controller.pi.PiMicroController;

public class RelayActor extends AbstractActor {

  private PiMicroController controller = PiMicroController.getInstance();

  public RelayActor() {
    super("R");
  }

  @Override
  public void process(SensorData data) {
    System.out.println("Relay Actor called ...");
    if ("true".equalsIgnoreCase(data.getValue())) {
      controller.setRedLedState(true);
      controller.setRelayState(true);
    } else {
      controller.setRedLedState(false);
      controller.setRelayState(false);
    }
  }

}
