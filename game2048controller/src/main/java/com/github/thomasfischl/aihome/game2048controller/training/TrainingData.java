package com.github.thomasfischl.aihome.game2048controller.training;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;

public class TrainingData {

  // private static NormalizerGame2048 normalizer = new NormalizerGame2048();

  private GameGrid grid;
  private Direction direction;

  public TrainingData(GameGrid grid, Direction direction) {
    this.grid = grid;
    this.direction = direction;
  }

  public TrainingData(String data, int dimension) {
    String[] values = data.split(",");
    grid = new GameGrid(dimension);

    int idx = 0;
    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        grid.setCell(j, i, Integer.parseInt(values[idx++]));
      }
    }

    int len = values.length;
    direction = Direction.fromString(values[len - 4] + values[len - 3] + values[len - 2] + values[len - 1]);
  }

  public GameGrid getGrid() {
    return grid;
  }

  public void setGrid(GameGrid grid) {
    this.grid = grid;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public String asString() {
    return getGridDataAsString(grid) + direction.asString();
  }

  private String getGridDataAsString(GameGrid grid) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        sb.append(grid.getCell(j, i)).append(",");
      }
    }
    return sb.toString();
  }
}
