package com.github.thomasfischl.aihome.controller.actors;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;

public abstract class AbstractActor {

  private final String name;

  public AbstractActor(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public abstract void process(SensorData data);
}
