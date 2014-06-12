package com.github.thomasfischl.aihome.controller;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

public abstract class AbstractPipelineService {

  private AbstractPipelineService nextService;

  public AbstractPipelineService(AbstractPipelineService nextService) {
    this.nextService = nextService;
  }

  public abstract void process(SensorDataGroup data);

  protected void executeNextService(SensorDataGroup data) {
    if (nextService != null) {
      nextService.process(data);
    }
  }

}
