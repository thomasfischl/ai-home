package com.github.thomasfischl.aihome.game2048controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import com.github.thomasfischl.aihome.brain.NormalizerGame2048;
import com.github.thomasfischl.aihome.game2048controller.GameController.KeyCode;

public class GameTrainer {

  private String sessionId;
  private BufferedWriter writer;

  private NormalizerGame2048 normalizer = new NormalizerGame2048();

  public GameTrainer(File folder) {

    // for (Entry<Integer, Double> entry : mapping.entrySet()) {
    // System.out.println(entry.getKey() + ": " + entry.getValue());
    // }

    // sessionId = UUID.randomUUID().toString();
    sessionId = String.valueOf(System.currentTimeMillis());
    try {
      folder.mkdirs();
      writer = new BufferedWriter(new FileWriter(new File(folder, sessionId + ".tdata")));
      writer.write("d0,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12,d13,d14,d15,out0,out1,out2,out3\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveTrainingData(GameGrid grid, KeyCode keyCode) {
    try {
      writer.append(getGridDataAsString(grid));
      writer.append(getKeyCodeAsString(keyCode));
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

  private String getGridDataAsString(GameGrid grid) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        Double val = normalizer.map(grid.getCell(j, i));
        sb.append(String.format(Locale.US, "%.2f", val)).append(",");
      }
    }
    return sb.toString();
  }

  private String getKeyCodeAsString(KeyCode key) {
    switch (key) {
    case DOWN:
      return "1,0,0,0,";
    case UP:
      return "0,1,0,0,";
    case LEFT:
      return "0,0,1,0,";
    case RIGHT:
      return "0,0,0,1,";
    }

    throw new IllegalArgumentException("Invalid key code:" + key);
  }
}
