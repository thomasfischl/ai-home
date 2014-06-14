package com.github.thomasfischl.aihome.brain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;

import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataStore;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;
import com.google.common.base.Joiner;

public class Application {

  public static void main(String[] args) {

    if (args.length != 4) {
      showUsage();
      System.exit(-1);
    }

    File inputFile = new File(args[0]);
    File outputFile = new File(args[1]);
    String[] inputNeuronNames = args[2].split(";");
    String[] outputNeuronNames = args[3].split(";");

    System.out.println("-----------------------------------------------------");
    System.out.println("--              AIHome - Brain                     --");
    System.out.println("-----------------------------------------------------");
    System.out.println("  InputFile:      " + inputFile.getAbsolutePath());
    System.out.println("  OutputFile:     " + outputFile.getAbsolutePath());
    System.out.println("  Input Neurons:  " + Joiner.on(",").join(inputNeuronNames));
    System.out.println("  Output Neurons: " + Joiner.on(",").join(outputNeuronNames));
    System.out.println("-----------------------------------------------------");
    System.out.println("  Loading training data ...");
    try {
      loadTrainingData(inputFile, inputNeuronNames, outputNeuronNames);
    } catch (IOException e) {
      System.err.println("Error during loading training data from input file.");
      throw new RuntimeException(e);
    }
    System.out.println("-----------------------------------------------------");

  }

  private static List<MLDataPair> loadTrainingData(File inputFile, String[] inputNeuronNames, String[] outputNeuronNames) throws IOException {
    SensorDataStore store = new SensorDataStore();
    List<MLDataPair> trainingDataList = new ArrayList<MLDataPair>();

    outer: for (SensorDataGroup sensorData : store.readSensorData(inputFile)) {
      BasicMLData inputData = new BasicMLData(inputNeuronNames.length);
      BasicMLData outputData = new BasicMLData(outputNeuronNames.length);

      for (int idx = 0; idx < inputNeuronNames.length; idx++) {
        String neuron = inputNeuronNames[idx];
        SensorData val = getSensorDataValue(sensorData, neuron);
        if (val == null) {
          System.out.println("Skip data line, because no value for the neuron '" + neuron + "' exits!");
          continue outer;
        }
        inputData.add(idx, transfromSensorDataValue(val));
      }

      for (int idx = 0; idx < outputNeuronNames.length; idx++) {
        String neuron = outputNeuronNames[idx];
        SensorData val = getSensorDataValue(sensorData, neuron);
        if (val == null) {
          System.out.println("Skip data line, because no value for the neuron '" + neuron + "' exits!");
          continue outer;
        }
        outputData.add(idx, transfromSensorDataValue(val));
      }

      trainingDataList.add(new BasicMLDataPair(inputData, outputData));
    }

    return trainingDataList;
  }

  private static double transfromSensorDataValue(SensorData val) {
    if (val.getType() == SensorDataType.BOOL) {
      if (val.getValue().equals("true")) {
        return 1;
      } else {
        return -1;
      }
    } else if (val.getType() == SensorDataType.ENUM) {
      return Double.valueOf(val.getValue());
    }
    throw new IllegalArgumentException("The sensor data type '" + val.getType().name() + "' is not supported.");
  }

  private static SensorData getSensorDataValue(SensorDataGroup data, String sensorName) {
    for (SensorData entry : data.getValues()) {
      if (sensorName.equals(entry.getValue())) {
        return entry;
      }
    }
    return null;
  }

  private static void showUsage() {
    System.out.println("usage: aihome-brain <input-file> <output-file> <input-neuron-names> <output-neuron-names>");
  }

}
