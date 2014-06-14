package com.github.thomasfischl.aihome.game2048controller;

import java.util.concurrent.Callable;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;
import com.github.thomasfischl.aihome.game2048controller.controller.GameState;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.util.GameGridConverter;

public class GameBrainTask implements Callable<GameGrid> {

  private IGameController controller;
  private Brain brain;
  private long thinkTime;
  private boolean log;
  private int unkownState;

  public GameBrainTask(IGameController controller, Brain brain, long thinkTime, boolean log) {
    this.controller = controller;
    this.brain = brain;
    this.thinkTime = thinkTime;
    this.log = log;
  }

  @Override
  public GameGrid call() {
    try {
      start();
      GameGrid lastGrid = null;
      while (controller.state() == GameState.RUNNING) {
        GameGrid grid = controller.getGrid();
        log(grid.toString());
        move(lastGrid, grid);
        lastGrid = grid;

        if (thinkTime != 0) {
          try {
            Thread.sleep(thinkTime);
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }
        }

      }

      reportResult();
      stop();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return controller.getGrid();
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
      switch (unkownState) {
      case 0:
        key = Direction.DOWN;
        break;
      case 1:
        key = Direction.RIGHT;
        break;
      case 2:
        key = Direction.LEFT;
        break;
      default:
        key = Direction.UP;
      }
      // key = Direction.values()[rand.nextInt(4)];
      log("Random move: " + key);
      unkownState++;
    } else {
      unkownState = 0;
      key = calculateBrainNextStep(grid);
    }
    return key;
  }

  protected Direction calculateBrainNextStep(GameGrid grid) {
    // double[] result = brain.compute(GameGridConverter.asGridArray(grid));
    double[] result = brain.compute(GameGridConverter.transform(grid));
    // double[] result = brain.compute(GameGridConverter.asFullArray(grid));

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