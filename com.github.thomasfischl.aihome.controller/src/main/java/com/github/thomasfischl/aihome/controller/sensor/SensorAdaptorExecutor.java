package com.github.thomasfischl.aihome.controller.sensor;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.controller.SensorAggregatorService;

public class SensorAdaptorExecutor implements Runnable {

  private AbstractSensorAdaptor adaptor;

  private SensorAggregatorService aggregatorService;

  public SensorAdaptorExecutor(AbstractSensorAdaptor adaptor, SensorAggregatorService aggregatorService) {
    super();
    this.adaptor = adaptor;
    this.aggregatorService = aggregatorService;
  }

  @Override
  public void run() {
    SensorData data = adaptor.process();
    aggregatorService.process(data);
  }
}
