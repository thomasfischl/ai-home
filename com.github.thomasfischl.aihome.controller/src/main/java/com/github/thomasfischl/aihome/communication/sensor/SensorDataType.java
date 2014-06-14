package com.github.thomasfischl.aihome.communication.sensor;

import java.util.Arrays;
import java.util.List;

public enum SensorDataType {
  BOOL(Boolean.TRUE.toString(), Boolean.FALSE.toString()), WEEKDAY("1", "2", "3", "4", "5", "6", "7"), HOUR_OF_DAY("0", "1", "2", "3", "4", "5",
      "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");

  private SensorDataType(String... values) {
    possibleValues = Arrays.asList(values);
  }

  private final List<String> possibleValues;

  public List<String> getPossibleValues() {
    return possibleValues;
  }
}
