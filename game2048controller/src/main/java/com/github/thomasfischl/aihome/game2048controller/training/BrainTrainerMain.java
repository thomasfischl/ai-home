package com.github.thomasfischl.aihome.game2048controller.training;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;

import com.github.thomasfischl.aihome.brain.TrainBrain;
import com.github.thomasfischl.aihome.game2048controller.util.GameGridConverter;

public class BrainTrainerMain {

  private File folder;

  public BrainTrainerMain(File folder) throws Exception {
    this.folder = folder;
  }

  public MLDataSet loadTrainingData() throws Exception {
    MLDataSet set = new BasicMLDataSet();
    int lineCount = 0;

    for (File f : folder.listFiles()) {
      if (lineCount > 1000) {
        break;
      }

      if (f.getName().endsWith(".tdata")) {

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
          br.readLine(); // skip header
          String line;
          while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
              continue;
            }
            processTrainingData(set, line);
            lineCount++;
          }
        }

      }
    }

    return set;
  }

  private void generate() throws Exception {
    MLDataSet data = loadTrainingData();
    TrainBrain trainer = new TrainBrain();
    trainer.train(data, 9, 4);
  }

  private void processTrainingData(MLDataSet set, String line) {
    TrainingData data = new TrainingData(line, 4);

    // -------
    System.out.println(data.getGrid());
    System.out.println(data.getDirection());
    double[] result = GameGridConverter.transform(data.getGrid());
    for (double val : result) {
      System.out.print(val + " - ");
    }
    System.out.println();

    // try {
    // while (System.in.read() != 13)
    // ;
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    boolean empty = true;
    for (double val : result) {
      if (val > 0) {
        empty = false;
      }
    }
    if (empty) {
      return;
    }

    // -------

    // BasicMLData inputData = new BasicMLData(result);
    // BasicMLData inputData = new BasicMLData(GameGridConverter.asGridArray(data.getGrid()));
    BasicMLData inputData = new BasicMLData(GameGridConverter.transform(data.getGrid()));
    // BasicMLData inputData = new BasicMLData(GameGridConverter.asFullArray(data.getGrid()));
    BasicMLData idealData = new BasicMLData(data.getDirection().asDoubleArray());
    set.add(inputData, idealData);

  }

  public static void main(String[] args) throws Exception {
    // BrainTrainerMain generator = new BrainTrainerMain(new File("./traindata"));
    BrainTrainerMain generator = new BrainTrainerMain(new File("./traindata/manuel"));
    generator.generate();
  }

}
