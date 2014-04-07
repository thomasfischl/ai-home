package com.github.thomasfischl.aihome.game2048controller.controller.java;

import java.util.Random;

class Tile {

  private Integer value;
  private Location location;
  private Boolean merged;

  public static Tile newRandomTile() {
    int value = new Random().nextDouble() < 0.9 ? 2 : 4;
    return new Tile(value);
  }

  public static Tile newTile(int value) {
    return new Tile(value);
  }

  private Tile(Integer value) {
    this.value = value;
    this.merged = false;
  }

  public void merge(Tile another) {
    this.value += another.getValue();
    merged = true;
  }

  public Integer getValue() {
    return value;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "Tile{" + "value=" + value + ", location=" + location + '}';
  }

  public boolean isMerged() {
    return merged;
  }

  public void clearMerge() {
    merged = false;
  }
}