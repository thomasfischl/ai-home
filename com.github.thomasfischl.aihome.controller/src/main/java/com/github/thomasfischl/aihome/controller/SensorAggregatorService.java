package com.github.thomasfischl.aihome.controller;

import java.util.HashMap;
import java.util.Map;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

public class SensorAggregatorService extends AbstractPipelineService implements Runnable {

  private Map<String, SensorData> values = new HashMap<String, SensorData>();

  public SensorAggregatorService(AbstractPipelineService nextService) {
    super(nextService);
  }

  public void process(SensorData data) {
//    System.out.println("process data: " + data);
    values.put(data.getName(), data);
  }

  @Override
  public void run() {
    executeNextService(new SensorDataGroup(values.values(), System.currentTimeMillis()));
  }

  @Override
  public void process(SensorDataGroup data) {
    executeNextService(new SensorDataGroup(values.values(), System.currentTimeMillis()));
  }
}
