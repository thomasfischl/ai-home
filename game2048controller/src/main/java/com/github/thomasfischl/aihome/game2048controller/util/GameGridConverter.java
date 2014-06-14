package com.github.thomasfischl.aihome.game2048controller.util;

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

      for (int col = 0; col < grid.getDimension(); col++) {
        for (int col1 = col; col1 < grid.getDimension(); col1++) {
          if (col == col1) {
            continue;
          }
          if (grid.getCell(col, row) == grid.getCell(col1, row) && grid.getCell(col, row) != 0) {
            found = true;
          }
        }
      }
      result[idx++] = found ? 1 : 0;
    }

    for (int col = 0; col < grid.getDimension(); col++) {
      boolean found = false;

      for (int row = 0; row < grid.getDimension(); row++) {
        for (int row1 = row; row1 < grid.getDimension(); row1++) {
          if (row == row1) {
            continue;
          }
          if (grid.getCell(col, row) == grid.getCell(col, row1) && grid.getCell(col, row) != 0) {
            found = true;
          }
        }
      }
      result[idx++] = found ? 1 : 0;
    }

    result[8] = GameGridUtil.findCluster(GameGridUtil.getFutureGrid(grid, Direction.UP)) ? 1 : 0;
    result[9] = GameGridUtil.findCluster(GameGridUtil.getFutureGrid(grid, Direction.LEFT)) ? 1 : 0;
    result[10] = GameGridUtil.findCluster(GameGridUtil.getFutureGrid(grid, Direction.DOWN)) ? 1 : 0;
    result[11] = GameGridUtil.findCluster(GameGridUtil.getFutureGrid(grid, Direction.RIGHT)) ? 1 : 0;

    return result;
  }

  public static double[] asFullArray1(GameGrid grid) {
    double[] part1 = asGridArray(grid);

    for (int i = 0; i < part1.length; i++) {
      double val = part1[i];

      // if (val > 0.6) {
      // part1[i] = 1;
      // } else
      if (val > 0) {
        part1[i] = 1;
      } else {
        part1[i] = 0.0;
      }
    }

    double[] part2 = asDirectionWithHintArray(grid);

    double[] result = new double[part1.length + part2.length];
    System.arraycopy(part1, 0, result, 0, part1.length);
    System.arraycopy(part2, 0, result, part1.length, part2.length);

    return result;
  }

  public static double[] transform(GameGrid grid) {
//     return asDirectionWithHintArray(grid);
    return asFullArray1(grid);
  }

}
