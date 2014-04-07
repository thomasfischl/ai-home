package com.github.thomasfischl.aihome.game2048controller.training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;
import com.github.thomasfischl.aihome.game2048controller.util.NormalizerGame2048;

public class TrainingDataCollector {

  private String sessionId;
  private BufferedWriter writer;

  private NormalizerGame2048 normalizer = new NormalizerGame2048();
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
      writer.append(getGridDataAsString(grid));
      writer.append(getDirectionCodeAsString(direction));
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

  private String getGridDataAsString(GameGrid grid) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        sb.append(normalizer.mapAsString(grid.getCell(j, i))).append(",");
      }
    }
    return sb.toString();
  }

  private String getDirectionCodeAsString(Direction direction) {
    switch (direction) {
    case DOWN:
      return "1,0,0,0,";
    case UP:
      return "0,1,0,0,";
    case LEFT:
      return "0,0,1,0,";
    case RIGHT:
      return "0,0,0,1,";
    }

    throw new IllegalArgumentException("Invalid direction:" + direction);
  }
}
