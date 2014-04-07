package com.github.thomasfischl.aihome.game2048controller.controller.java;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;

class Location {

  private final int x;
  private final int y;

  public Location(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Location offset(Direction direction) {
    return new Location(x + direction.getX(), y + direction.getY());
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public String toString() {
    return "Location{" + "x=" + x + ", y=" + y + '}';
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + this.x;
    hash = 97 * hash + this.y;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Location other = (Location) obj;
    if (this.x != other.x) {
      return false;
    }
    return this.y == other.y;
  }

  public boolean isValidFor(int gridSize) {
    return x >= 0 && x < gridSize && y >= 0 && y < gridSize;
  }

}