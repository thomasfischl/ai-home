package com.github.thomasfischl.aihome.brain;

import java.util.ArrayList;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;

public class SensorDataHelper {

  public static SensorDataGroup createSensorDataGroup(boolean btn, int t, int wd, boolean bt, boolean r) {
    SensorDataGroup data = new SensorDataGroup(new ArrayList<SensorData>(), 0);
    data.getValues().add(new SensorData("BTN", String.valueOf(btn), SensorDataType.BOOL));
    data.getValues().add(new SensorData("T", "" + t, SensorDataType.ENUM));
    data.getValues().add(new SensorData("WD", "" + wd, SensorDataType.ENUM));
    data.getValues().add(new SensorData("BT", String.valueOf(bt), SensorDataType.BOOL));
    data.getValues().add(new SensorData("R", String.valueOf(r), SensorDataType.BOOL));
    return data;
  }
}
