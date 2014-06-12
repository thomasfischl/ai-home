package com.github.thomasfischl.aihome.controller.rule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;

public class RuleEngineTest {

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  @Test
  public void testSimple() throws IOException {
    RuleEngine engine = createDemoData();

    ArrayList<SensorData> values = new ArrayList<SensorData>();
    values.add(new SensorData("XYZ", "false", SensorDataType.BOOL));
    SensorDataGroup result = engine.evaluate(new SensorDataGroup(values, 0));
    Assert.assertEquals(2, result.getValues().size());
    Assert.assertEquals("Z", result.getValues().get(1).getName());
    Assert.assertEquals("1", result.getValues().get(1).getValue());
  }

  @Test
  public void testComplex() throws IOException {
    RuleEngine engine = createDemoData();

    ArrayList<SensorData> values = new ArrayList<SensorData>();
    values.add(new SensorData("BT", "true", SensorDataType.BOOL));
    values.add(new SensorData("BTN", "true", SensorDataType.BOOL));
    SensorDataGroup result = engine.evaluate(new SensorDataGroup(values, 0));
    Assert.assertEquals(3, result.getValues().size());
    Assert.assertEquals("r", result.getValues().get(2).getName());
    Assert.assertEquals("true", result.getValues().get(2).getValue());
  }

  @Test
  public void testNoActiveRule() throws IOException {
    RuleEngine engine = createDemoData();

    ArrayList<SensorData> values = new ArrayList<SensorData>();
    values.add(new SensorData("BT", "true", SensorDataType.BOOL));
    values.add(new SensorData("BTN", "false", SensorDataType.BOOL));
    SensorDataGroup result = engine.evaluate(new SensorDataGroup(values, 0));
    Assert.assertEquals(2, result.getValues().size());
  }

  private RuleEngine createDemoData() throws IOException {
    File propFile = testFolder.newFile("rule.properties");

    StringBuilder sb = new StringBuilder();
    sb.append("rule1.BT = true \n");
    sb.append("rule1.BTN = true\n");
    sb.append("rule1.result.r = true\n");
    sb.append("\n");
    sb.append("rule2.XYZ = false \n");
    sb.append("rule2.result.Z = 1\n");
    FileUtils.write(propFile, sb.toString());
    RuleEngine engine = new RuleEngine(propFile);
    return engine;
  }

}
