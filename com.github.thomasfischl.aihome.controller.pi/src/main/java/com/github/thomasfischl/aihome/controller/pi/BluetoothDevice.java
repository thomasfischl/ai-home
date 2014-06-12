package com.github.thomasfischl.aihome.controller.pi;

public class BluetoothDevice {

  private final String name;

  private final String macAddress;

  public BluetoothDevice(String name, String macAddress) {
    super();
    this.name = name;
    this.macAddress = macAddress;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name + "(" + macAddress + ")";
  }

}
