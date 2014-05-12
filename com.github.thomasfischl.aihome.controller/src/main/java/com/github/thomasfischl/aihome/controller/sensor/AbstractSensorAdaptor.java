package com.github.thomasfischl.aihome.controller.sensor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.controller.ScheduleService;

public abstract class AbstractSensorAdaptor {

  private String name;

  private String type;

  private long schedulePeriod;

  private MessageChannel channel;

  @Autowired
  private ScheduleService scheduleService;

  @PostConstruct
  public void init() {
    scheduleService.scheduleSensor(this, getSchedulePeriod());
  }

  public void sendToChannel() {
    Message<SensorData> message = MessageBuilder.withPayload(process()).build();
    channel.send(message);
  }

  public void setChannel(MessageChannel channel) {
    this.channel = channel;
  }

  protected String getName() {
    return name;
  }

  @Required
  public void setName(String name) {
    this.name = name;
  }

  protected String getType() {
    return type;
  }

  @Required
  public void setType(String type) {
    this.type = type;
  }

  @Required
  public void setSchedulePeriod(long schedulePeriod) {
    this.schedulePeriod = schedulePeriod;
  }

  protected long getSchedulePeriod() {
    return schedulePeriod;
  }

  public abstract SensorData process();

}
