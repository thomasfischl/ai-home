package com.github.thomasfischl.aihome.brain;

import java.io.File;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

public class TrainBrain {

  public void train(MLDataSet trainData, int inputNeurons, int outputNeurons) {
    // create a neural network, without using a factory
    BasicNetwork network = new BasicNetwork();
    network.addLayer(new BasicLayer(null, true, inputNeurons));
    network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 32));
     network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 32));
    // network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 32));
    network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputNeurons));
    network.getStructure().finalizeStructure();
    network.reset();

    // train the neural network
    final ResilientPropagation train = new ResilientPropagation(network, trainData);
    int epoch = 1;

    do {
      train.iteration();
      if (epoch % 100 == 0) {
        System.out.format("Epoch # %d Error: %.2f\n", epoch, train.getError() * 100);
      }
      epoch++;
    } while (train.getError() > 0.05 && epoch < 10000);
    train.finishTraining();

    // test the neural network
    System.out.println("Neural Network Results:");
    for (MLDataPair pair : trainData) {
      final MLData output = network.compute(pair.getInput());

      MLData ideal = pair.getIdeal();
      String in = directionAsString(ideal);
      String out = directionAsString(output);
      System.out.println(in + " <==> " + out + "// " + (in.equals(out)));
    }

    double e = network.calculateError(trainData);
    System.out.println("Network trained to error: " + (e * 100));

    EncogDirectoryPersistence.saveObject(new File("./network/nn.eg"), network);

    Encog.getInstance().shutdown();
  }

  private String directionAsString(MLData data) {
    double max = 0;
    for (int i = 0; i < data.size(); i++) {
      max = Math.max(max, data.getData(i));
    }

    String direction = "";
    for (int i = 0; i < data.size(); i++) {
      if (data.getData(i) == max) {
        direction += "1";
      } else {
        direction += "0";
      }
    }
    return direction;
  }
}
