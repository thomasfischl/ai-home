package com.github.thomasfischl.aihome.controller.sensor;

import java.io.IOException;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;
import com.github.thomasfischl.aihome.controller.pi.BluetoothDevice;
import com.github.thomasfischl.aihome.controller.pi.PiMicroController;

public class BluetoothSearcherSensorAdaptor extends AbstractSensorAdaptor {

  private PiMicroController controller = PiMicroController.getInstance();

  private final String macAddress;

  public BluetoothSearcherSensorAdaptor(String macAddress) {
    super("BT", SensorDataType.BOOL);
    this.macAddress = macAddress;
  }

  @Override
  public SensorData process() {
    try {
      for (BluetoothDevice device : controller.getAllBluetoothDevices()) {
        System.out.println("MacAddress: " + macAddress);
        if (macAddress.equalsIgnoreCase(device.getMacAddress())) {
          return createSensorData(Boolean.toString(true));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return createSensorData(Boolean.toString(false));
  }

}
