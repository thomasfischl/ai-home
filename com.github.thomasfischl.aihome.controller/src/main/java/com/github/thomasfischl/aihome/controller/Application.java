package com.github.thomasfischl.aihome.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {

  private SensorAggregatorService sensorAggregatorService = new SensorAggregatorService();

  private ScheduledExecutorService pool;

  public void start() {
    pool = Executors.newScheduledThreadPool(10);
    pool.scheduleAtFixedRate(sensorAggregatorService, 10, 10, TimeUnit.SECONDS);
    
    //TODO start all sensor adaptors
    
  }

  public static void main(String[] args) {

  }
}
