package com.github.thomasfischl.aihome.game2048controller;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.controller.java.JavaGameController;
import com.github.thomasfischl.aihome.game2048controller.training.TrainingDataCollector;

public class GameTrainerDataCollectorMain {

  private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);

  public static void main(String[] args) throws Exception {
    for (int i = 0; i < 100000; i++) {
      executeGame();
    }
  }

  private static void executeGame() {
    Brain brain = new Brain();
    final IGameController controller = new JavaGameController();
    controller.start();

    final TrainingDataCollector collector = new TrainingDataCollector(new File("./traindata"));

    pool.submit(new GameBrainTask(controller, brain, 2, false) {

      @Override
      protected void start() {
        collector.start();
      }

      @Override
      protected void move(GameGrid lastGrid, GameGrid grid) {
        Direction key = calculateNextStep(lastGrid, grid);
        collector.saveTrainingData(grid, key);
        controller.move(key);
      }

      @Override
      protected void reportResult() {
        if (controller.getGrid().highNumber() > 250) {
          super.reportResult();
        }
      }

      @Override
      protected void stop() {
        // pool.shutdown();
        collector.stop();
        if (controller.getGrid().highNumber() < 250) {
          collector.deleteDataFile();
        }
      }

    });
  }

}
