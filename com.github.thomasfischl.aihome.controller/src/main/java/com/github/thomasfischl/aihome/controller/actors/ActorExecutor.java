package com.github.thomasfischl.aihome.controller.actors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.controller.AbstractPipelineService;

public class ActorExecutor extends AbstractPipelineService implements Runnable {

  private List<AbstractActor> actors = new ArrayList<>();

  private Queue<SensorDataGroup> backlog = new LinkedList<>();

  public ActorExecutor(AbstractPipelineService nextService) {
    super(nextService);
  }

  public void addActor(AbstractActor actor) {
    if (actor != null) {
      System.out.println("Activate Actor: " + actor.getClass().getSimpleName());
      actors.add(actor);
    }
  }

  @Override
  public void process(SensorDataGroup data) {
    backlog.offer(data);
    executeNextService(data);
  }

  @Override
  public void run() {
    SensorDataGroup data = null;
    while ((data = backlog.poll()) != null) {
      for (AbstractActor actor : actors) {
        for (SensorData sensorData : data.getValues()) {
          if (sensorData.getName().equals(actor.getName())) {
            actor.process(sensorData);
          }
        }
      }
    }
  }

}
