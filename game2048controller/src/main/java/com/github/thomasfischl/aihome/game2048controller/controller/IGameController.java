package com.github.thomasfischl.aihome.game2048controller.controller;

public interface IGameController {

  void start();

  void stop();

  GameGrid getGrid();

  void move(Direction direction);

  GameState state();

}
