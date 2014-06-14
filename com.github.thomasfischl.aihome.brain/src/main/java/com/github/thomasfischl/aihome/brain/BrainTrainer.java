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

public class BrainTrainer {

  private BasicNetwork network;

  private int hiddenLayers = 0;

  private int numberOfNeuronsHL = 0;

  public BrainTrainer(int hiddenLayers, int numberOfNeuronsHL) {
    super();
    this.hiddenLayers = hiddenLayers;
    this.numberOfNeuronsHL = numberOfNeuronsHL;
  }

  public BasicNetwork createNetwork(MLDataPair data) {
    return createNetwork(data.getInput().size(), data.getIdeal().size());
  }

  public BasicNetwork createNetwork(int inputNeurons, int outputNeurons) {
    BasicNetwork network = new BasicNetwork();
    network.addLayer(new BasicLayer(null, true, inputNeurons));

    for (int i = 0; i < hiddenLayers; i++) {
      network.addLayer(new BasicLayer(new ActivationSigmoid(), true, numberOfNeuronsHL));
    }

    network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputNeurons));
    network.getStructure().finalizeStructure();
    network.reset();
    return network;
  }

  public void advancedTraining(List<MLDataPair> data, final double ratio, final int maxEpoch) {
    network = createNetwork(data.get(0));

    Collections.shuffle(data);

    MLDataSet trainData = new BasicMLDataSet();
    MLDataSet testData = new BasicMLDataSet();

    int trainDataCount = (int) (data.size() * ratio);
    System.out.println("TrainingData Count: " + trainDataCount);
    for (int i = 0; i < data.size(); i++) {
      if (i < trainDataCount) {
        trainData.add(data.get(i));
      } else {
        testData.add(data.get(i));
      }
    }

    network = training(network, trainData, testData, maxEpoch);
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
          testError = network.calculateError(testData);
        }

        System.out.format("Epoch # %d Error: %.2f (%.2f)\n", epoch, train.getError(), testError);
      }
      epoch++;
    } while (train.getError() > 0.05 && epoch < maxEpoch);
    train.finishTraining();

    double e = network.calculateError(trainData);
    System.out.println("Network trained to error: " + (e * 100));

    return network;
  }

  public void saveNetwork(File networkFile) {
    networkFile.getParentFile().mkdirs();
    if (networkFile.exists()) {
      networkFile.delete();
    }
    EncogDirectoryPersistence.saveObject(networkFile, network);
    Encog.getInstance().shutdown();
  }

  public BasicNetwork getNetwork() {
    return network;
  }

}
