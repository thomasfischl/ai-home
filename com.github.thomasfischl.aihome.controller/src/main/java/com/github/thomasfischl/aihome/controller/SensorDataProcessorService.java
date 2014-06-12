package com.github.thomasfischl.aihome.controller;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;

public class SensorDataProcessorService extends AbstractPipelineService {

  public SensorDataProcessorService(AbstractPipelineService nextService) {
    super(nextService);
  }

  public void process(SensorDataGroup data) {

    List<SensorData> newData = new ArrayList<>();
    
    for (SensorData d : data.getValues()) {
      SensorData newSensorData = evaluate(d);
      if (newSensorData != null) {
        newData.add(newSensorData);
      }
    }

    data.getValues().addAll(newData);
    executeNextService(data);
  }

  private SensorData evaluate(SensorData data) {
    if ("BT".equals(data.getName())) {

      if ("true".equals(data.getValue())) {
        return new SensorData("R", "true", SensorDataType.BOOL);
      } else {
        return new SensorData("R", "false", SensorDataType.BOOL);
      }

    }

    return null;
  }

}
