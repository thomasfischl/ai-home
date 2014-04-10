package com.github.thomasfischl.aihome.game2048controller.util;

import java.util.HashSet;
import java.util.Set;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;

public class GameGridConverter {

  private static NormalizerGame2048 norm = new NormalizerGame2048();

  public static double[] asGridArray(GameGrid grid) {
    double[] result = new double[grid.getDimension() * grid.getDimension()];
    int idx = 0;
    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        result[idx++] = norm.map(grid.getCell(i, j));
      }
    }

    return result;
  }

  public static double[] asDirectionArray(GameGrid grid) {
    double[] result = new double[grid.getDimension() * 2];
    int idx = 0;

    for (int row = 0; row < grid.getDimension(); row++) {
      boolean found = false;
      // int val = 0;

      for (int col = 0; col < grid.getDimension(); col++) {
        for (int col1 = col; col1 < grid.getDimension(); col1++) {
          if (col == col1) {
            continue;
          }
          if (grid.getCell(col, row) == grid.getCell(col1, row) && grid.getCell(col, row) != 0) {
            // System.out.println(grid.getCell(col, row) + " == " + grid.getCell(col1, row));
            found = true;
            // val = Math.max(val, grid.getCell(col1, row));
          }
        }
      }
      result[idx++] = found ? 1 : 0;
      // result[idx++] = norm.map(val);
    }

    for (int col = 0; col < grid.getDimension(); col++) {
      boolean found = false;
      // int val = 0;

      for (int row = 0; row < grid.getDimension(); row++) {
        for (int row1 = row; row1 < grid.getDimension(); row1++) {
          if (row == row1) {
            continue;
          }
          if (grid.getCell(col, row) == grid.getCell(col, row1) && grid.getCell(col, row) != 0) {
            // System.out.println(grid.getCell(col, row) + " == " + grid.getCell(col, row1));
            found = true;
            // val = Math.max(val, grid.getCell(col, row));
          }
        }
      }
      result[idx++] = found ? 1 : 0;
      // result[idx++] = norm.map(val);
    }

    // for (int i = 0; i < result.length; i++) {
    // System.out.print(result[i] + ",");
    // }
    // System.out.println();
    return result;
  }

  public static double[] asFullArray(GameGrid grid) {
    double[] result = new double[grid.getDimension() * grid.getDimension() + (grid.getDimension() * 2)];

    double[] part1 = asGridArray(grid);
    double[] part2 = asDirectionArray(grid);
    System.arraycopy(part1, 0, result, 0, part1.length);
    System.arraycopy(part2, 0, result, part1.length, part2.length);

    // for (int i = 0; i < result.length; i++) {
    // System.out.print(result[i] + ",");
    // }
    // System.out.println();
    return result;
  }

  public static double[] asDirectionWithHintArray(GameGrid grid) {
    double[] result = new double[grid.getDimension() * 2 + 4];
    int idx = 0;

    for (int row = 0; row < grid.getDimension(); row++) {
      boolean found = false;
      // int val = 0;

      for (int col = 0; col < grid.getDimension(); col++) {
        for (int col1 = col; col1 < grid.getDimension(); col1++) {
          if (col == col1) {
            continue;
          }
          if (grid.getCell(col, row) == grid.getCell(col1, row) && grid.getCell(col, row) != 0) {
            // System.out.println(grid.getCell(col, row) + " == " + grid.getCell(col1, row));
            found = true;
            // val = Math.max(val, grid.getCell(col1, row));
          }
        }
      }
      result[idx++] = found ? 1 : 0;
      // result[idx++] = norm.map(val);
    }

    for (int col = 0; col < grid.getDimension(); col++) {
      boolean found = false;
      // int val = 0;

      for (int row = 0; row < grid.getDimension(); row++) {
        for (int row1 = row; row1 < grid.getDimension(); row1++) {
          if (row == row1) {
            continue;
          }
          if (grid.getCell(col, row) == grid.getCell(col, row1) && grid.getCell(col, row) != 0) {
            // System.out.println(grid.getCell(col, row) + " == " + grid.getCell(col, row1));
            found = true;
            // val = Math.max(val, grid.getCell(col, row));
          }
        }
      }
      result[idx++] = found ? 1 : 0;
      // result[idx++] = norm.map(val);
    }

    boolean found = findCluster(grid, Direction.UP);
    result[8] = found ? 1 : 0;

    // for (int i = 0; i < result.length; i++) {
    // System.out.print(result[i] + ",");
    // }
    // System.out.println();
    return result;
  }

  private static boolean findCluster(GameGrid grid, Direction direction) {
    boolean found = false;

    grid = rotateGrid(grid, direction);

    for (int row = 0; row < grid.getDimension(); row++) {
      if (grid.getCell(grid.getDimension() - 1, row) != 0) {
        continue;
      }
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

  public static double[] asFullArray1(GameGrid grid) {
    double[] result = new double[grid.getDimension() * grid.getDimension() + (grid.getDimension() * 2) + 1];

    double[] part1 = asGridArray(grid);

    for (int i = 0; i < part1.length; i++) {
      double val = part1[i];

      if (val > 0.6) {
        part1[i] = 0.75;
      } else if (val > 0) {
        part1[i] = 0.5;
      } else {
        part1[i] = 0.0;
      }
    }

    double[] part2 = asDirectionWithHintArray(grid);

    System.arraycopy(part1, 0, result, 0, part1.length);
    System.arraycopy(part2, 0, result, part1.length, part2.length);

    // for (int i = 0; i < result.length; i++) {
    // System.out.print(result[i] + ",");
    // }
    // System.out.println();
    return result;
  }

  public static double[] transform(GameGrid grid) {
    // return asDirectionWithHintArray(grid);
    return asFullArray1(grid);
  }

  public static GameGrid getFutureGrid(GameGrid g, Direction d) {
    GameGrid grid = g.copy();
    int rotate = 0;
    grid = rotateGrid(grid, d);
    Set<String> merged = new HashSet<>();

    int dim = grid.getDimension();
    for (int row = 0; row < dim; row++) {
      for (int col = 0; col < dim; col++) {
        for (int i = 0; i < row; i++) {
          String key = col + "-" + i;

          if (grid.getCell(col, i) == 0) {
            // move
            grid.setCell(col, i, grid.getCell(col, row));
            grid.setCell(col, row, 0);

          } else if (grid.getCell(col, i) == grid.getCell(col, row) && !merged.contains(key)) {
            // merge
            System.out.println("merge: " + grid.getCell(col, row));
            grid.setCell(col, i, grid.getCell(col, row) * 2);
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
      grid = rotateGrid(grid);
    }

    return grid;
  }

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
      grid = rotateGrid(grid);
    }

    return grid;
  }

  public static GameGrid rotateGrid(GameGrid grid) {
    GameGrid result = new GameGrid(grid.getDimension());

    int d = grid.getDimension();
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < d; j++) {
        result.setCell(i, j, grid.getCell(j, d - i - 1));
      }
    }

    return result;
  }

}
