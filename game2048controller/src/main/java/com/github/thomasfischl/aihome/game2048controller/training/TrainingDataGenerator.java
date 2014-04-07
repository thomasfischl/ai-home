package com.github.thomasfischl.aihome.game2048controller.training;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TrainingDataGenerator {

  private BufferedWriter writer;
  private File trainDataFile;

  public TrainingDataGenerator(File folder) throws Exception {
    trainDataFile = new File(folder, "training-data.csv");
    if (trainDataFile.exists()) {
      trainDataFile.delete();
    }
  }

  public void generate() throws Exception {
    writer = new BufferedWriter(new FileWriter(trainDataFile));
    writer.write(TrainingDataUtil.HEADER);

    int lineCount = 0;

    for (File f : trainDataFile.getParentFile().listFiles()) {
      if (lineCount > 1000) {
        break;
      }

      if (f.getName().endsWith(".tdata")) {

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
          br.readLine(); // skip header

          String line;
          while ((line = br.readLine()) != null) {
            writer.append(line).append("\n");
            lineCount++;
          }
        }

      }
    }

    writer.close();
  }

  public static void main(String[] args) throws Exception {
    TrainingDataGenerator generator = new TrainingDataGenerator(new File("./traindata"));
    generator.generate();
  }
}
