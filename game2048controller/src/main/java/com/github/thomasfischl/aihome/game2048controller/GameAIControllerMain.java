package com.github.thomasfischl.aihome.game2048controller;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.controller.selenium.SeleniumGameController;

public class GameAIControllerMain {

  private static IGameController controller;
  private static Brain brain;

  public static void main(String[] args) throws Exception {
    brain = new Brain();
    // controller = new JavaGameController();
    controller = new SeleniumGameController("http://gabrielecirulli.github.io/2048/");
    controller.start();
    new GameBrainTask(controller, brain, 150, true).call();
  }

}
