package com.github.thomasfischl.aihome.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.google.gson.Gson;

public class SensorDataWriterService extends AbstractPipelineService {

  private File dataStore;

  private BufferedWriter writer;

  public SensorDataWriterService(AbstractPipelineService nextService) {
    super(nextService);

    writer = null;
    try {
      dataStore = new File("/tmp/data-store.csv");
      writer = new BufferedWriter(new FileWriter(dataStore, true));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void storeData(SensorDataGroup data) {
    try {
      writer.write(new Gson().toJson(data));
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void process(SensorDataGroup data) {
    storeData(data);
    executeNextService(data);
  }

}
