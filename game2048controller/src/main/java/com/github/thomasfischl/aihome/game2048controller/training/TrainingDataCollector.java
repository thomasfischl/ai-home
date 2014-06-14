package com.github.thomasfischl.aihome.game2048controller.training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;

public class TrainingDataCollector {

  private String sessionId;
  private BufferedWriter writer;

  private File file;

  public TrainingDataCollector(File folder) {
    // sessionId = UUID.randomUUID().toString();
    sessionId = String.valueOf(System.currentTimeMillis());
    folder.mkdirs();
    file = new File(folder, sessionId + ".tdata");
  }

  public void start() {
    try {
      writer = new BufferedWriter(new FileWriter(file));
      writer.write(TrainingDataUtil.HEADER);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveTrainingData(GameGrid grid, Direction direction) {
    try {
      TrainingData data = new TrainingData(grid, direction);
      writer.append(data.asString());
      writer.append("\n");
      writer.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteDataFile() {
    if (file != null) {
      file.delete();
    }
  }

}
