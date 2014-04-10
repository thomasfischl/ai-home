package com.github.thomasfischl.aihome.brain;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

public class TrainBrain {

  private File folder = new File("./network");

  public BasicNetwork createNetwork(MLDataPair data) {
    return createNetwork(data.getInput().size(), data.getIdeal().size());
  }

  public BasicNetwork createNetwork(int inputNeurons, int outputNeurons) {
    // create a neural network, without using a factory
    BasicNetwork network = new BasicNetwork();
    network.addLayer(new BasicLayer(null, true, inputNeurons));
    network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 32));
    network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 64));
    network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 32));
    // network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 32));
    network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputNeurons));
    network.getStructure().finalizeStructure();
    network.reset();
    return network;
  }

  public void advancedTraining(List<MLDataPair> data, final double ratio, final int retry, final int maxEpoch) {
    BasicNetwork network = createNetwork(data.get(0));

    int retryCount = 0;
    while (retryCount < retry) {

      Collections.shuffle(data);

      MLDataSet trainData = new BasicMLDataSet();
      MLDataSet testData = new BasicMLDataSet();

      int trainDataCount = (int) (data.size() * ratio);
      for (int i = 0; i < data.size(); i++) {
        if (i < trainDataCount) {
          trainData.add(data.get(i));
        } else {
          testData.add(data.get(i));
        }
      }
      network = training(network, trainData, testData, maxEpoch);
      retryCount++;
    }
    saveNetwork(network);
  }

  private BasicNetwork training(BasicNetwork network, MLDataSet trainData, MLDataSet testData, int maxEpoch) {

    // train the neural network
    final ResilientPropagation train = new ResilientPropagation(network, trainData);
    int epoch = 1;
    do {
      double testError = -1;
      train.iteration();

      if (epoch % 100 == 0) {
        if (testData != null) {
          testError = network.calculateError(testData) * 100;
        }

        try {
          Thread.sleep(4);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
        System.out.format("Epoch # %d Error: %.2f (%.2f)\n", epoch, train.getError() * 100, testError);
      }
      epoch++;
    } while (train.getError() > 0.05 && epoch < maxEpoch);
    train.finishTraining();

    double e = network.calculateError(trainData);
    System.out.println("Network trained to error: " + (e * 100));

    return network;
  }

  private void saveNetwork(BasicNetwork network) {
    folder.mkdirs();
    File networkFile = new File(folder, "nn.eg");
    if (networkFile.exists()) {
      networkFile.delete();
    }
    EncogDirectoryPersistence.saveObject(networkFile, network);
    Encog.getInstance().shutdown();
  }

  // private String directionAsString(MLData data) {
  // double max = 0;
  // for (int i = 0; i < data.size(); i++) {
  // max = Math.max(max, data.getData(i));
  // }
  //
  // String direction = "";
  // for (int i = 0; i < data.size(); i++) {
  // if (data.getData(i) == max) {
  // direction += "1";
  // } else {
  // direction += "0";
  // }
  // }
  // return direction;
  // }
}
