package com.github.thomasfischl.aihome.game2048controller.controller;

public enum Direction {

  UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

  private final int y;
  private final int x;

  Direction(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String asString() {
    switch (this) {
    case DOWN:
      return "1,0,0,0,";
    case UP:
      return "0,1,0,0,";
    case LEFT:
      return "0,0,1,0,";
    case RIGHT:
      return "0,0,0,1,";
    }

    throw new IllegalArgumentException("Invalid direction:" + this);
  }

  public double[] asDoubleArray() {
    switch (this) {
    case DOWN:
      return new double[] { 1, 0, 0, 0 };
    case UP:
      return new double[] { 0, 1, 0, 0 };
    case LEFT:
      return new double[] { 0, 0, 1, 0 };
    case RIGHT:
      return new double[] { 0, 0, 0, 1 };
    }

    throw new IllegalArgumentException("Invalid direction:" + this);
  }

  public static Direction fromString(String data) {
    switch (data) {
    case "1000":
      return Direction.DOWN;
    case "0100":
      return Direction.UP;
    case "0010":
      return Direction.LEFT;
    case "0001":
      return Direction.RIGHT;
    }

    throw new IllegalArgumentException("Invalid direction:" + data);
  }

}