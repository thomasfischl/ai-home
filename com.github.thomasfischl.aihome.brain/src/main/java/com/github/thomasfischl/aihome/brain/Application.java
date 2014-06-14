package com.github.thomasfischl.aihome.brain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.neural.networks.BasicNetwork;

import com.github.thomasfischl.aihome.brain.rule.RuleCreator;
import com.github.thomasfischl.aihome.brain.rule.RuleNode;
import com.github.thomasfischl.aihome.communication.sensor.SensorData;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataStore;
import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;
import com.google.common.base.Joiner;

public class Application {

  public static void main(String[] args) {
    List<MLDataPair> trainingData;

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
      trainingData = loadTrainingData(inputFile, inputNeuronNames, outputNeuronNames);
      System.out.println(trainingData.size() + " items loaded");
    } catch (IOException e) {
      System.err.println("Error during loading training data from input file.");
      throw new RuntimeException(e);
    }
    System.out.println("-----------------------------------------------------");
    System.out.println("  Train neuronal network ...");
    BrainTrainer trainer = new BrainTrainer(2, 10);
    trainer.advancedTraining(trainingData, 0.7, 10000, 0.001);
    System.out.println("-----------------------------------------------------");
    System.out.println("  Test neuronal network ...");

    analyzeNetwork(inputNeuronNames, outputNeuronNames, trainer);

    Encog.getInstance().shutdown();
    System.out.println("Finish");
  }

  private static void analyzeNetwork(String[] inputNeuronNames, String[] outputNeuronNames, BrainTrainer trainer) {
    RuleCreator creater = new RuleCreator();

    for (int weekDay = Calendar.SUNDAY; weekDay <= Calendar.SATURDAY; weekDay++) {
      for (int hour = 0; hour < 24; hour++) {
        for (int bt = 0; bt <= 1; bt++) {
          SensorDataGroup sensorData = SensorDataHelper.createSensorDataGroup(false, hour, weekDay, bt == 1, true);
          BasicNetwork network = trainer.getNetwork();
          MLData result = network.compute(fillMLData(sensorData, inputNeuronNames, outputNeuronNames).getInput());

          // check if relay is on
          if (result.getData(0) > result.getData(1)) {
            RuleNode btNode = new RuleNode("BT", getSensorDataValue(sensorData, "BT").getValue(), SensorDataType.BOOL);
            RuleNode tNode = new RuleNode("T", getSensorDataValue(sensorData, "T").getValue(), SensorDataType.HOUR_OF_DAY);
            RuleNode wdNode = new RuleNode("WD", getSensorDataValue(sensorData, "WD").getValue(), SensorDataType.WEEKDAY);

            btNode.addChild(wdNode);
            wdNode.addChild(tNode);
            creater.addTree(btNode);
          } else if (bt == 1) {
            System.out.println(weekDay + " " + hour);
          }
        }
      }
    }

    creater.merge();
    creater.generateRules();
  }

  private static List<MLDataPair> loadTrainingData(File inputFile, String[] inputNeuronNames, String[] outputNeuronNames)
      throws IOException {
    SensorDataStore store = new SensorDataStore();
    List<MLDataPair> trainingDataList = new ArrayList<MLDataPair>();

    for (SensorDataGroup sensorData : store.readSensorData(inputFile)) {
      BasicMLDataPair data = fillMLData(sensorData, inputNeuronNames, outputNeuronNames);
      if (data != null) {
        trainingDataList.add(data);
      }
    }
    return trainingDataList;
  }

  private static BasicMLDataPair fillMLData(SensorDataGroup sensorData, String[] inputNeuronNames, String[] outputNeuronNames) {
    BasicMLData inputData = new BasicMLData(inputNeuronNames.length);

    //
    // load data for input neurons
    //
    for (int idx = 0; idx < inputNeuronNames.length; idx++) {
      String neuron = inputNeuronNames[idx];
      SensorData val = getSensorDataValue(sensorData, neuron);
      if (val == null) {
        System.out.println("Skip data line, because no value for the neuron '" + neuron + "' exists!");
        return null;
      }
      inputData.add(idx, transfromSensorDataValue(val));
    }

    //
    // load data for output neurons
    //
    if (outputNeuronNames.length > 1) {
      throw new IllegalArgumentException("Only one output neuron is allowed");
    }

    BasicMLData outputData = new BasicMLData(2);
    String neuron = outputNeuronNames[0];
    SensorData val = getSensorDataValue(sensorData, neuron);
    if (val == null) {
      System.out.println("Skip data line, because no value for the neuron '" + neuron + "' exists!");
      return null;
    }

    outputData.add(0, "true".equals(val.getValue()) ? 1 : 0);
    outputData.add(1, "true".equals(val.getValue()) ? 0 : 1);

    return new BasicMLDataPair(inputData, outputData);
  }

  private static double transfromSensorDataValue(SensorData val) {
    if (val.getType() == SensorDataType.BOOL) {
      if (val.getValue().equals("true")) {
        return 1;
      } else {
        return 0;
      }
    } else if (val.getType() == SensorDataType.HOUR_OF_DAY) {
      return Double.valueOf(val.getValue()) / 23;
    } else if (val.getType() == SensorDataType.WEEKDAY) {
      return (Double.valueOf(val.getValue()) - 1) / 6;
    }
    throw new IllegalArgumentException("The sensor data type '" + val.getType().name() + "' is not supported.");
  }

  private static SensorData getSensorDataValue(SensorDataGroup data, String sensorName) {
    for (SensorData entry : data.getValues()) {
      if (sensorName.equals(entry.getName())) {
        return entry;
      }
    }
    return null;
  }

  private static void showUsage() {
    System.out.println("usage: aihome-brain <input-file> <output-file> <input-neuron-names> <output-neuron-names>");
  }

}
