package com.github.thomasfischl.aihome.controller.rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

public class RuleEngine {

  private File propFile = new File("/tmp/rule.properties");

  private long lastChangedAt = -1;

  private Map<String, Rule> rules = new LinkedHashMap<>();

  public RuleEngine(File propFile) {
    this.propFile = propFile;
  }

  public void loadRules() {
    try {
      Properties prop = new Properties();
      prop.load(new FileInputStream(propFile));

      Set<String> names = prop.stringPropertyNames();

      for (String name : names) {
        String[] parts = name.split("\\.");
        String ruleName = parts[0].trim();
        if (!rules.containsKey(ruleName)) {
          System.out.println("Add new rule: " + ruleName);
          rules.put(ruleName, new Rule(ruleName));
        }

        Rule rule = rules.get(ruleName);

        String value = prop.getProperty(name).trim();
        if (parts.length == 2) {
          rule.addCondition(new Condition(parts[1].trim(), value));
        } else if ("result".equals(parts[1].trim())) {
          rule.addResult(new Result(parts[2].trim(), value));
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public SensorDataGroup evaluate(SensorDataGroup data) {

    if (lastChangedAt < propFile.lastModified()) {
      lastChangedAt = propFile.lastModified() + 100;
      System.out.println("Reload rules");
      loadRules();
    }

    List<SensorData> sensorDatas = new ArrayList<>(data.getValues());
    for (Rule rule : rules.values()) {
      if (rule.evaluate(data)) {
        for (Result r : rule.getResults()) {
          // System.out.println("Add new result: " + r.getName());
          sensorDatas.add(r.getSensorData());
        }
      }
    }

    return new SensorDataGroup(sensorDatas, data.getTimestamp());
  }

}
