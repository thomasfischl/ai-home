package com.github.thomasfischl.aihome.brain;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

public class Brain {

  private BasicNetwork network;

  public Brain() {
    network = (BasicNetwork) EncogDirectoryPersistence.loadResourceObject("simple.eg");
  }

  public double[] compute(double[] value) {
    BasicMLData data = new BasicMLData(value);
    MLData result = network.compute(data);
    return result.getData();
  }

}
