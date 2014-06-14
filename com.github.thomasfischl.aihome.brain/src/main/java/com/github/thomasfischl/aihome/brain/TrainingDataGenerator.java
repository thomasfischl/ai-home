package com.github.thomasfischl.aihome.brain;

import java.io.File;
import java.util.Calendar;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataStore;

public class TrainingDataGenerator {

  public static void main(String[] args) {

    File fileStore = new File("./sample/demo1.txt");
    fileStore.getParentFile().mkdirs();
    SensorDataStore store = new SensorDataStore(fileStore);

    // week 1
    generateSampleData(store, 18, 20, Calendar.MONDAY);
    generateSampleData(store, 19, 20, Calendar.TUESDAY);
    generateSampleData(store, 0, 0, Calendar.WEDNESDAY);
    generateSampleData(store, 18, 21, Calendar.THURSDAY);
    generateSampleData(store, 20, 22, Calendar.FRIDAY);
    generateSampleData(store, 7, 10, Calendar.SATURDAY);
    generateSampleData(store, 8, 10, Calendar.SUNDAY);

    // week 2
    generateSampleData(store, 19, 20, Calendar.MONDAY);
    generateSampleData(store, 17, 19, Calendar.TUESDAY);
    generateSampleData(store, 20, 22, Calendar.WEDNESDAY);
    generateSampleData(store, 0, 0, Calendar.THURSDAY);
    generateSampleData(store, 20, 22, Calendar.FRIDAY);
    generateSampleData(store, 9, 11, Calendar.SATURDAY);
    generateSampleData(store, 8, 11, Calendar.SUNDAY);

  }

  private static void generateSampleData(SensorDataStore store, int fromHour, int toHour, int weekday) {
    for (int i = 0; i < 24; i++) {
      if (fromHour <= i && i <= toHour) {
        store.storeData(SensorDataHelper.createSensorDataGroup(false, i, weekday, true, true));
      } else {
        store.storeData(SensorDataHelper.createSensorDataGroup(false, i, weekday, false, false));
      }
    }
  }

}
