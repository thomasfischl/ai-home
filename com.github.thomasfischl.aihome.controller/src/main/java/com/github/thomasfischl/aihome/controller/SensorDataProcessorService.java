package com.github.thomasfischl.aihome.controller;

import java.io.File;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.controller.rule.RuleEngine;

public class SensorDataProcessorService extends AbstractPipelineService {

  private RuleEngine engine = new RuleEngine(new File("/home/pi/aihome/rule.config"));

  public SensorDataProcessorService(AbstractPipelineService nextService) {
    super(nextService);
  }

  public void process(SensorDataGroup data) {
    SensorDataGroup newData = engine.evaluate(data);
    executeNextService(newData);
  }

}
