package com.github.thomasfischl.aihome.game2048controller.util;

import java.util.HashSet;
import java.util.Set;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;

public class GameGridUtil {

  public static GameGrid rotateGrid(GameGrid grid, Direction d) {
    int rotate = 0;

    switch (d) {
    case UP:
      rotate = 0;
      break;
    case LEFT:
      rotate = 3;
      break;
    case RIGHT:
      rotate = 1;
      break;
    case DOWN:
      rotate = 2;
      break;
    }
    for (int i = 0; i < rotate; i++) {
      grid = rotateGridRight(grid);
    }

    return grid;
  }

  public static GameGrid rotateGridRight(GameGrid grid) {
    GameGrid result = new GameGrid(grid.getDimension());

    int d = grid.getDimension();
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < d; j++) {
        result.setCell(i, j, grid.getCell(j, d - i - 1));
      }
    }

    return result;
  }

  public static GameGrid getFutureGrid(GameGrid g, Direction d) {
    GameGrid grid = g.copy();
    int rotate = 0;
    grid = rotateGrid(grid, d);
    Set<String> merged = new HashSet<>();

//    System.out.println(grid);
    int dim = grid.getDimension();
    for (int row = 0; row < dim; row++) {
      for (int col = 0; col < dim; col++) {

        int pos = -1;

        for (int i = row - 1; i >= 0; i--) {
          String key = col + "-" + i;

          if (grid.getCell(col, i) == 0) {
            // move
            // grid.setCell(col, i, grid.getCell(col, row));
            // grid.setCell(col, row, 0);
            pos = i;
          } else if (grid.getCell(col, i) == grid.getCell(col, row) && !merged.contains(key)) {
            // merge
            // System.out.println("merge: " + grid.getCell(col, row));
            // grid.setCell(col, i, grid.getCell(col, row) * 2);
            // grid.setCell(col, row, 0);
            // merged.add(key);
            pos = i;
          } else {
            break;
          }
        }

        if (pos != -1) {
          if (grid.getCell(col, pos) == 0) {
            grid.setCell(col, pos, grid.getCell(col, row));
            grid.setCell(col, row, 0);
          } else {
//            System.out.println("merge: " + col + "/" + row + " = " + col + "/" + pos);
            String key = col + "-" + pos;
            grid.setCell(col, pos, grid.getCell(col, row) * 2);
            grid.setCell(col, row, 0);
            merged.add(key);
          }
        }

      }
    }

    switch (d) {
    case UP:
      rotate = 0;
      break;
    case LEFT:
      rotate = 1;
      break;
    case RIGHT:
      rotate = 3;
      break;
    case DOWN:
      rotate = 2;
      break;
    }
    for (int i = 0; i < rotate; i++) {
      grid = rotateGridRight(grid);
    }

    return grid;
  }

  public static boolean findCluster(GameGrid grid) {
    if (findCluster(grid, true)) {
      return true;
    }
    return findCluster(grid, false);
  }

  public static boolean findCluster(GameGrid grid, boolean vertical) {
    boolean found = false;

    if (!vertical) {
      grid = rotateGrid(grid, Direction.LEFT);
    }
    // System.out.println(grid);
    for (int row = 0; row < grid.getDimension(); row++) {
      for (int col = 0; col < grid.getDimension(); col++) {
        int val = grid.getCell(col, row);
        if (val == 0) {
          continue;
        }
        if (grid.isCellValid(col, row + 1) && val == grid.getCell(col, row + 1)) {
          found = true;
        }
      }
    }
    return found;
  }

  public static boolean canMove(GameGrid grid, Direction direction) {
    // GameGrid g = rotateGrid(grid, direction);
    // for (int i = 0; i < grid.getDimension(); i++) {
    // if (g.getCell(i, 0) == 0) {
    // return true;
    // }
    // }
    // return false;

    GameGrid g = getFutureGrid(grid, direction);

    for (int row = 0; row < grid.getDimension(); row++) {
      for (int col = 0; col < grid.getDimension(); col++) {
        if (grid.getCell(col, row) != g.getCell(col, row)) {
          return true;
        }
      }
    }

    return false;
  }
}
