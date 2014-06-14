package com.github.thomasfischl.aihome.game2048controller.util;

import org.junit.Assert;
import org.junit.Test;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;

public class GameGridUtilTest {
  @Test
  public void test1() {
    GameGrid grid = new GameGrid(4);
    grid.setCell(3, 0, 2);
    grid.setCell(2, 1, 2);
    grid.setCell(3, 1, 8);
    grid.setCell(1, 2, 2);
    grid.setCell(2, 2, 4);
    grid.setCell(3, 2, 2);
    grid.setCell(1, 3, 4);
    grid.setCell(2, 3, 2);
    grid.setCell(3, 3, 4);
    System.out.println(grid);
    System.out.println(GameGridUtil.getFutureGrid(grid, Direction.DOWN));
    Assert.assertFalse(GameGridUtil.canMove(grid, Direction.DOWN));
  }
  
}
