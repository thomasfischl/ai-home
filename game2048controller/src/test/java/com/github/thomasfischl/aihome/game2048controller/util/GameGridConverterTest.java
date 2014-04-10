package com.github.thomasfischl.aihome.game2048controller.util;

import org.junit.Test;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;

public class GameGridConverterTest {

  @Test
  public void test() {
    GameGrid grid = new GameGrid(4);
    grid.setCell(1, 2, 1);
    grid.setCell(2, 2, 1);
    grid.setCell(0, 0, 1);
    grid.setCell(0, 3, 2);
    System.out.println(grid);

    System.out.println(GameGridConverter.getFutureGrid(grid, Direction.UP));
    System.out.println(GameGridConverter.getFutureGrid(grid, Direction.LEFT));
    System.out.println(GameGridConverter.getFutureGrid(grid, Direction.DOWN));
    System.out.println(GameGridConverter.getFutureGrid(grid, Direction.RIGHT));
  }

  @Test
  public void test1() {
    System.out.println("---------------------------------------------------------");
    GameGrid grid = new GameGrid(4);
    grid.setCell(1, 2, 1);
    grid.setCell(2, 2, 1);
    grid.setCell(0, 0, 1);
    grid.setCell(0, 1, 1);
    grid.setCell(0, 3, 2);
    System.out.println(grid);
    
    double[] result = GameGridConverter.asDirectionWithHintArray(grid);
    for (double val : result) {
      System.out.print(val + ", ");
    }
    System.out.println();
  }
}
