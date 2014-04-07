package com.github.thomasfischl.aihome.game2048controller;

import java.util.Random;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;
import com.github.thomasfischl.aihome.game2048controller.controller.GameState;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.util.NormalizerGame2048;

public class GameBrainTask implements Runnable {

  private Random rand = new Random();
  private IGameController controller;
  private Brain brain;
  private static NormalizerGame2048 normalizer = new NormalizerGame2048();

  public GameBrainTask(IGameController controller, Brain brain) {
    this.controller = controller;
    this.brain = brain;
  }

  @Override
  public void run() {
    try {
      GameGrid lastGrid = null;
      while (controller.state() == GameState.RUNNING) {
        GameGrid grid = controller.getGrid();
        move(lastGrid, grid);
        lastGrid = grid;
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
          break;
        }

        System.out.println(grid);
      }

      reportResult();
      GameTrainerMain.pool.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void reportResult() {
    GameGrid grid = controller.getGrid();
    System.out.println("Finished:   " + controller.state());
    System.out.println("Score:      " + grid.score());
    System.out.println("HighNumber: " + grid.highNumber());
    System.out.println(grid);
  }

  private void move(GameGrid lastGrid, GameGrid grid) {
    Direction key;
    if (grid.equals(lastGrid)) {
      key = Direction.values()[rand.nextInt(4)];
      System.out.println("Random move");
    } else {
      key = calculateNextStep(grid);
    }

    controller.move(key);
  }

  private Direction calculateNextStep(GameGrid grid) {

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
    System.out.println("Next move: " + key);

    return key;
  }
}