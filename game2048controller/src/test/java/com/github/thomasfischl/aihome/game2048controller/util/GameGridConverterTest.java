package com.github.thomasfischl.aihome.game2048controller.util;

import org.junit.Assert;
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

    GameGrid result = GameGridUtil.getFutureGrid(grid, Direction.UP);
    Assert.assertEquals(1, result.getCell(0, 0));
    Assert.assertEquals(1, result.getCell(1, 0));
    Assert.assertEquals(1, result.getCell(2, 0));
    Assert.assertEquals(2, result.getCell(0, 1));

    result = GameGridUtil.getFutureGrid(grid, Direction.LEFT);
    Assert.assertEquals(1, result.getCell(3, 0));
    Assert.assertEquals(2, result.getCell(3, 2));
    Assert.assertEquals(2, result.getCell(3, 3));

    result = GameGridUtil.getFutureGrid(grid, Direction.DOWN);
    Assert.assertEquals(1, result.getCell(0, 2));
    Assert.assertEquals(2, result.getCell(0, 3));
    Assert.assertEquals(1, result.getCell(1, 3));
    Assert.assertEquals(1, result.getCell(2, 3));

    result = GameGridUtil.getFutureGrid(grid, Direction.RIGHT);
    Assert.assertEquals(1, result.getCell(0, 0));
    Assert.assertEquals(2, result.getCell(0, 2));
    Assert.assertEquals(2, result.getCell(0, 3));
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

    Assert.assertEquals(0, result[0], 0.0);
    Assert.assertEquals(0, result[1], 0.0);
    Assert.assertEquals(1, result[2], 0.0);
    Assert.assertEquals(0, result[3], 0.0);
    Assert.assertEquals(1, result[4], 0.0);
    Assert.assertEquals(0, result[5], 0.0);
    Assert.assertEquals(0, result[6], 0.0);
    Assert.assertEquals(0, result[7], 0.0);
    Assert.assertEquals(1, result[8], 0.0);
    Assert.assertEquals(1, result[9], 0.0);
    Assert.assertEquals(1, result[10], 0.0);
    Assert.assertEquals(1, result[11], 0.0);
  }

  @Test
  public void test2() {
    System.out.println("---------------------------------------------------------");
    GameGrid grid = new GameGrid(4);
    grid.setCell(1, 2, 1);
    grid.setCell(2, 2, 1);
    System.out.println(grid);

    double[] result = GameGridConverter.asDirectionWithHintArray(grid);
    for (double val : result) {
      System.out.print(val + ", ");
    }
    System.out.println();

    Assert.assertEquals(0, result[0], 0.0);
    Assert.assertEquals(0, result[1], 0.0);
    Assert.assertEquals(1, result[2], 0.0);
    Assert.assertEquals(0, result[3], 0.0);
    Assert.assertEquals(0, result[4], 0.0);
    Assert.assertEquals(0, result[5], 0.0);
    Assert.assertEquals(0, result[6], 0.0);
    Assert.assertEquals(0, result[7], 0.0);
    Assert.assertEquals(1, result[8], 0.0);
    Assert.assertEquals(0, result[9], 0.0);
    Assert.assertEquals(1, result[10], 0.0);
    Assert.assertEquals(0, result[11], 0.0);
  }

  @Test
  public void test3() {
    GameGrid grid = new GameGrid(4);
    grid.setCell(1, 2, 1);
    grid.setCell(2, 2, 1);
    grid.setCell(3, 2, 1);
    System.out.println(grid);

    GameGrid result = GameGridUtil.getFutureGrid(grid, Direction.LEFT);
    System.out.println(result);
    Assert.assertEquals(1, result.getCell(2, 2));
    Assert.assertEquals(2, result.getCell(3, 2));

    result = GameGridUtil.getFutureGrid(grid, Direction.RIGHT);
    Assert.assertEquals(2, result.getCell(0, 2));
    Assert.assertEquals(1, result.getCell(1, 2));
  }

  @Test
  public void test4() {
    GameGrid grid = new GameGrid(4, new int[][] { { 0, 2, 8, 32 }, { 2, 4, 8, 64 }, { 2, 8, 16, 8 }, { 0, 4, 32, 4 } });
    System.out.println(grid);
    System.out.println(GameGridUtil.getFutureGrid(grid, Direction.RIGHT));

    Assert.assertFalse(GameGridUtil.findCluster(GameGridUtil.getFutureGrid(grid, Direction.RIGHT), true));
  }

  @Test
  public void test5() {
    GameGrid grid = new GameGrid(4, new int[][] { { 0, 0, 0, 8 }, { 2, 0, 0, 16 }, { 0, 4, 8, 64 }, { 2, 2, 16, 32 } });
    System.out.println(grid);
    System.out.println(GameGridUtil.getFutureGrid(grid, Direction.RIGHT));

    Assert.assertTrue(GameGridUtil.findCluster(GameGridUtil.getFutureGrid(grid, Direction.RIGHT), true));
    Assert.assertFalse(GameGridUtil.findCluster(GameGridUtil.getFutureGrid(grid, Direction.RIGHT), false));
  }

}
