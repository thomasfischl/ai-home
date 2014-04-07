package com.github.thomasfischl.aihome.game2048controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class NormalizeTrainData {

  private String inFile = "./traindata/b30255e7-1b69-42cd-9dbe-a34ea3cc065c.tdata";

  private Map<String, String> mapping = new HashMap<>();

  public NormalizeTrainData() throws Exception {

//    mapping.put(arg0, arg1)
    
    try (BufferedReader br = new BufferedReader(new FileReader(inFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(inFile + ".normalized.csv"))) {

      bw.write(br.readLine());
      bw.newLine();

      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(";");
        for (String part : parts) {

        }

      }

    }

  }

}
