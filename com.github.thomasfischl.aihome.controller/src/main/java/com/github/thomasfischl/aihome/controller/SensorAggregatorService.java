package com.github.thomasfischl.aihome.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

@Service("sensorAggregator")
public class SensorAggregatorService {

  private Map<String, SensorData> values = new HashMap<String, SensorData>();

  public void process(SensorData data) {
    System.out.println("process data: " + data.getValue());

    String key = data.getName();
    if (values.containsKey(key)) {
      SensorData tmp = values.get(key);
      if (data.getTime() > tmp.getTime()) {
        System.out.println("Update value '" + key + "' to '" + data.getValue() + "'");
        values.put(key, data);
      }
    } else {
      values.put(key, data);
    }

  }

  public SensorDataGroup send() {
    System.out.println("send group");
    return new SensorDataGroup(values.values());
  }

}
