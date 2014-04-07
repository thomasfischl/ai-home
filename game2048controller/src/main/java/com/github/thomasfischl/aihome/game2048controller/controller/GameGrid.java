package com.github.thomasfischl.aihome.game2048controller.controller;

import com.google.common.base.Preconditions;

public class GameGrid {

  private int[][] grid;
  private int dimension;

  public GameGrid(int dimension) {
    this.dimension = dimension;
    grid = new int[dimension][dimension];
    clear();
  }

  public void clear() {
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        grid[i][j] = 0;
      }
    }
  }

  public int getCell(int col, int row) {
    Preconditions.checkArgument(col >= 0 && col < dimension);
    Preconditions.checkArgument(row >= 0 && row < dimension);
    return grid[col][row];
  }

  public void setCell(int col, int row, int value) {
    Preconditions.checkArgument(col >= 0 && col < dimension);
    Preconditions.checkArgument(row >= 0 && row < dimension);
    grid[col][row] = value;
  }

  public boolean isCellValid(int col, int row) {
    return col >= 0 && col < dimension && row >= 0 && row < dimension;
  }

  public int getDimension() {
    return dimension;
  }

  public int score() {
    int score = 0;
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        score += grid[i][j];
      }
    }
    return score;
  }

  public int highNumber() {
    int highNumber = 0;
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        highNumber = Math.max(grid[i][j], highNumber);
      }
    }
    return highNumber;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;

    GameGrid other = (GameGrid) obj;
    if (dimension != other.dimension) {
      return false;
    }
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (grid[i][j] != other.grid[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public String toString() {

    StringBuffer sb = new StringBuffer();

    drawLine(sb);
    for (int i = 0; i < dimension; i++) {
      sb.append("|");
      for (int j = 0; j < dimension; j++) {
        if (j > 0) {
          sb.append("|");
        }
        if (grid[j][i] > 100) {
          sb.append(" " + grid[j][i] + " ");
        } else if (grid[j][i] > 10) {
          sb.append("  " + grid[j][i] + " ");
        } else if (grid[j][i] > 0) {
          sb.append("   " + grid[j][i] + " ");
        } else {
          sb.append("     ");
        }
      }
      sb.append("|\n");
      drawLine(sb);
    }

    return sb.toString();
  }

  private void drawLine(StringBuffer sb) {
    sb.append("|");
    for (int i = 0; i < dimension - 1; i++) {
      sb.append("------");
    }
    sb.append("-----|\n");
  }

}
