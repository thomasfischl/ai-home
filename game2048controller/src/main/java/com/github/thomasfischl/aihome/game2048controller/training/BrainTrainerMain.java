package com.github.thomasfischl.aihome.game2048controller.training;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;

import com.github.thomasfischl.aihome.brain.TrainBrain;
import com.github.thomasfischl.aihome.game2048controller.util.GameGridConverter;

public class BrainTrainerMain {

  private File folder;

  public BrainTrainerMain(File folder) throws Exception {
    this.folder = folder;
  }

  public List<MLDataPair> loadTrainingData() throws Exception {
    List<MLDataPair> data = new ArrayList<MLDataPair>();
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
            data.add(processTrainingData(line));
            lineCount++;
          }
        }
      }
    }

    return data;
  }

  private void generate() throws Exception {
    List<MLDataPair> data = loadTrainingData();

    TrainBrain trainer = new TrainBrain();
    trainer.advancedTraining(data, 0.7, 1, 6000);
  }

  private MLDataPair processTrainingData(String line) {
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

    // boolean empty = true;
    // for (double val : result) {
    // if (val > 0) {
    // empty = false;
    // }
    // }
    // if (empty) {
    // return;
    // }

    // -------

    BasicMLData inputData = new BasicMLData(GameGridConverter.transform(data.getGrid()));
    BasicMLData idealData = new BasicMLData(data.getDirection().asDoubleArray());
    return new BasicMLDataPair(inputData, idealData);
  }

  public static void main(String[] args) throws Exception {
    // BrainTrainerMain generator = new BrainTrainerMain(new File("./traindata"));
    BrainTrainerMain generator = new BrainTrainerMain(new File("./traindata/manuel"));
    generator.generate();
  }

}
