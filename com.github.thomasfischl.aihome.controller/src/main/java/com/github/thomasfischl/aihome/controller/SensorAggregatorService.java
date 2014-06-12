package com.github.thomasfischl.aihome.controller;

import java.util.HashMap;
import java.util.Map;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

public class SensorAggregatorService implements Runnable {

  private Map<String, SensorData> values = new HashMap<String, SensorData>();

  private SensorDataWriterService writerService;

  public SensorAggregatorService(SensorDataWriterService writerService) {
    this.writerService = writerService;
  }

  public void process(SensorData data) {
    System.out.println("process data: " + data);

    String key = data.getName();
    // if (values.containsKey(key)) {
    // SensorData tmp = values.get(key);
    // if (data.getTime() > tmp.getTime()) {
    // System.out.println("Update value '" + key + "' to '" + data.getValue() + "'");
    // values.put(key, data);
    // }
    // } else {
    values.put(key, data);
    // }
  }

  @Override
  public void run() {
    writerService.storeData(new SensorDataGroup(values.values(), System.currentTimeMillis()));
  }
}
