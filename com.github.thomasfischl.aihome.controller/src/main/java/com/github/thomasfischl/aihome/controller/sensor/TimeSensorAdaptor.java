package com.github.thomasfischl.aihome.controller.sensor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;

public class TimeSensorAdaptor extends AbstractSensorAdaptor {

  public TimeSensorAdaptor() {
    super("Time", SensorDataType.ENUM);
  }

  @Override
  public SensorData process() {
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(new Date(System.currentTimeMillis()));
    return createSensorData(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
  }

}
