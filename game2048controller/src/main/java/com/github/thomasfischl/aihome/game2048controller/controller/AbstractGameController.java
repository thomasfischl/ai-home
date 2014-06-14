package com.github.thomasfischl.aihome.game2048controller.controller;

public abstract class AbstractGameController implements IGameController {

  private static final int FINAL_VALUE_TO_WIN = 2048;
  // private static final int DEFAULT_GRID_SIZE = 4;

  private int dimension;

  public AbstractGameController(int dimension) {
    this.dimension = dimension;
  }

  public int getDimension() {
    return dimension;
  }

  @Override
  public GameState state() {
    GameGrid grid = getGrid();

    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        int val = grid.getCell(i, j);
        if (val == 0) {
          return GameState.RUNNING;
        }
        if (val == FINAL_VALUE_TO_WIN) {
          return GameState.WIN;
        }

        if (grid.isCellValid(i, j - 1) && val == grid.getCell(i, j - 1)) {
          return GameState.RUNNING;
        }
        if (grid.isCellValid(i, j + 1) && val == grid.getCell(i, j + 1)) {
          return GameState.RUNNING;
        }
        if (grid.isCellValid(i - 1, j) && val == grid.getCell(i - 1, j)) {
          return GameState.RUNNING;
        }
        if (grid.isCellValid(i + 1, j) && val == grid.getCell(i + 1, j)) {
          return GameState.RUNNING;
        }

      }
    }

    return GameState.LOSE;
  }

}
