package com.github.thomasfischl.aihome.communication.sensor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class SensorDataStore {

  private File dataStore;

  private BufferedWriter writer;

  public SensorDataStore() {
    dataStore = new File("/tmp/data-store.csv");
  }

  public void storeData(SensorDataGroup data) {
    if (writer == null) {
      writer = null;
      try {
        writer = new BufferedWriter(new FileWriter(dataStore, true));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      writer.write(new Gson().toJson(data));
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<SensorDataGroup> readSensorData() throws IOException {
    List<SensorDataGroup> result = new ArrayList<>();
    List<String> lines = FileUtils.readLines(dataStore);

    Gson g = new Gson();
    for (String line : lines) {
      result.add(g.fromJson(line, SensorDataGroup.class));
    }

    return result;
  }

}
