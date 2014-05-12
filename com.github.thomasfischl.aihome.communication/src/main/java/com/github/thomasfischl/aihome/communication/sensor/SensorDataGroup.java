package com.github.thomasfischl.aihome.communication.sensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Joiner;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SensorDataGroup {

  private List<SensorData> values = new ArrayList<SensorData>();

  public SensorDataGroup() {
  }

  public SensorDataGroup(Collection<SensorData> values) {
    this.values = new ArrayList<SensorData>(values);
  }

  public List<SensorData> getValues() {
    return values;
  }

  @Override
  public String toString() {
    return "SensorDataGroup: " + Joiner.on(",").join(values);
  }
}
