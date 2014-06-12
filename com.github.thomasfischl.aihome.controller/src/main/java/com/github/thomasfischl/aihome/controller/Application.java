package com.github.thomasfischl.aihome.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.thomasfischl.aihome.controller.sensor.AbstractSensorAdaptor;
import com.github.thomasfischl.aihome.controller.sensor.SensorAdaptorExecutor;
import com.github.thomasfischl.aihome.controller.sensor.TimeSensorAdaptor;

public class Application {

  private SensorAggregatorService sensorAggregatorService = new SensorAggregatorService();

  private ScheduledExecutorService pool;

  public void start() {
    pool = Executors.newScheduledThreadPool(10);
    pool.scheduleAtFixedRate(sensorAggregatorService, 10, 500, TimeUnit.MILLISECONDS);
    schedule(new TimeSensorAdaptor(), 250);
  }

  private void schedule(AbstractSensorAdaptor adaptor, long rate) {
    pool.scheduleAtFixedRate(new SensorAdaptorExecutor(adaptor, sensorAggregatorService), 10, rate, TimeUnit.MILLISECONDS);
  }

  public static void main(String[] args) {
    Application app = new Application();
    app.start();
  }
}
