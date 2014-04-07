package com.github.thomasfischl.aihome.game2048controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.controller.java.JavaGameController;

public class GameTrainerMain {

  private static IGameController controller;
  private static Brain brain;
  private static ScheduledExecutorService pool;

  public static void main(String[] args) throws Exception {
    brain = new Brain();
    pool = Executors.newScheduledThreadPool(1);

    controller = new JavaGameController();
    // controller = new
    // SeleniumGameController("http://gabrielecirulli.github.io/2048/");
    controller.start();
    pool.submit(new GameBrainTask(controller, brain, 100, true) {
      @Override
      protected void stop() {
        pool.shutdown();
      }
    });
  }

}
