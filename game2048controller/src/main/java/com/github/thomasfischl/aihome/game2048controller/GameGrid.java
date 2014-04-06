package com.github.thomasfischl.aihome.game2048controller;

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
  
  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer();
    
    drawLine(sb);
    for (int i = 0; i < dimension; i++) {
      sb.append("|");
      for (int j = 0; j < dimension; j++) {
        if(j>0){
          sb.append("|");
        }
        sb.append(" " + grid[j][i] + " ");
      }
      sb.append("|\n");
      drawLine(sb);
    }
  
    return sb.toString();
  }

  private void drawLine(StringBuffer sb) {
    sb.append("|");
    for (int i = 0; i < dimension-1; i++) {
      sb.append("----");
    }
    sb.append("---|\n");
  }
  

}
