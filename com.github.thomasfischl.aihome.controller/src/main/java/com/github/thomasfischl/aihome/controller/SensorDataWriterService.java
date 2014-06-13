package com.github.thomasfischl.aihome.controller;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataStore;

public class SensorDataWriterService extends AbstractPipelineService {

  private SensorDataStore store = new SensorDataStore();

  public SensorDataWriterService(AbstractPipelineService nextService) {
    super(nextService);
  }

  @Override
  public void process(SensorDataGroup data) {
    store.storeData(data);
    executeNextService(data);
  }

}
