package com.github.thomasfischl.aihome.game2048controller;

import java.util.Random;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;
import com.github.thomasfischl.aihome.game2048controller.controller.GameState;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.util.NormalizerGame2048;

public class GameBrainTask implements Runnable {

  private static NormalizerGame2048 normalizer = new NormalizerGame2048();
  private Random rand = new Random();
  private IGameController controller;
  private Brain brain;
  private long thinkTime;
  private boolean log;

  public GameBrainTask(IGameController controller, Brain brain, long thinkTime, boolean log) {
    this.controller = controller;
    this.brain = brain;
    this.thinkTime = thinkTime;
    this.log = log;
  }

  @Override
  public void run() {
    try {
      start();
      GameGrid lastGrid = null;
      while (controller.state() == GameState.RUNNING) {
        GameGrid grid = controller.getGrid();
        move(lastGrid, grid);
        lastGrid = grid;
        try {
          Thread.sleep(thinkTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
          break;
        }

        log(grid.toString());
      }

      reportResult();
      stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void start() {
    // nothing to do
  }

  protected void stop() {
    // nothing to do
  }

  protected void log(String msg) {
    if (log) {
      System.out.println(msg);
    }
  }

  protected void reportResult() {
    GameGrid grid = controller.getGrid();
    System.out.println("Finished:   " + controller.state());
    System.out.println("Score:      " + grid.score());
    System.out.println("HighNumber: " + grid.highNumber());
    System.out.println(grid);
  }

  protected void move(GameGrid lastGrid, GameGrid grid) {
    Direction key = calculateNextStep(lastGrid, grid);
    controller.move(key);
  }

  protected Direction calculateNextStep(GameGrid lastGrid, GameGrid grid) {
    Direction key;
    if (grid.equals(lastGrid)) {
      key = Direction.values()[rand.nextInt(4)];
      log("Random move");
    } else {
      key = calculateBrainNextStep(grid);
    }
    return key;
  }

  protected Direction calculateBrainNextStep(GameGrid grid) {

    double[] data = new double[16];
    int idx = 0;
    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        double val = normalizer.map(grid.getCell(j, i));
        data[idx++] = val;
      }
    }

    double[] result = brain.compute(data);

    double max = -1;
    Direction key = Direction.LEFT;

    if (result[0] > max) {
      key = Direction.DOWN;
      max = result[0];
    }

    if (result[1] > max) {
      key = Direction.UP;
      max = result[1];
    }

    if (result[2] > max) {
      key = Direction.LEFT;
      max = result[2];
    }

    if (result[3] > max) {
      key = Direction.RIGHT;
      max = result[3];
    }
    log("Next move: " + key);

    return key;
  }
}