package com.github.thomasfischl.aihome.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.thomasfischl.aihome.controller.pi.PiMicroController;
import com.github.thomasfischl.aihome.controller.sensor.AbstractSensorAdaptor;
import com.github.thomasfischl.aihome.controller.sensor.BluetoothSearcherSensorAdaptor;
import com.github.thomasfischl.aihome.controller.sensor.GPIOSensorAdaptor;
import com.github.thomasfischl.aihome.controller.sensor.SensorAdaptorExecutor;
import com.github.thomasfischl.aihome.controller.sensor.TimeSensorAdaptor;

public class Application {

  private SensorAggregatorService sensorAggregatorService;

  private ScheduledExecutorService pool;

  private PiMicroController controller = PiMicroController.getInstance();

  private SensorDataWriterService writerService;

  public void start() {
    initPhase();

    writerService = new SensorDataWriterService();
    sensorAggregatorService = new SensorAggregatorService(writerService);

    pool = Executors.newScheduledThreadPool(10);
    pool.scheduleAtFixedRate(new PidFileWatcher(), 0, 2, TimeUnit.SECONDS);
    pool.scheduleAtFixedRate(sensorAggregatorService, 5000, 500, TimeUnit.MILLISECONDS);
    pool.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        controller.setGreenLedState(!controller.isGreenLedOn());
      }
    }, 0, 2, TimeUnit.SECONDS);

    schedule(new TimeSensorAdaptor(), 250);
    schedule(new GPIOSensorAdaptor(), 250);
    schedule(new BluetoothSearcherSensorAdaptor("BC:F5:AC:9C:4D:8A"), 1000);
  }

  private void initPhase() {
    try {

      for (int i = 0; i < 5; i++) {
        controller.setGreenLedState(true);
        Thread.sleep(500);
        controller.setYellowLedState(true);
        Thread.sleep(500);
        controller.setRedLedState(true);
        Thread.sleep(500);
        controller.setGreenLedState(false);
        controller.setYellowLedState(false);
        controller.setRedLedState(false);
        Thread.sleep(500);
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void schedule(AbstractSensorAdaptor adaptor, long rate) {
    pool.scheduleAtFixedRate(new SensorAdaptorExecutor(adaptor, sensorAggregatorService), 10, rate, TimeUnit.MILLISECONDS);
  }

  public static void main(String[] args) {
    System.out.println("Starting AI-Home Controller ...");
    Application app = new Application();
    app.start();
  }
}
