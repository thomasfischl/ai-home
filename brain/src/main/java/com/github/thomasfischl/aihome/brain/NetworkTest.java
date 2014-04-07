package com.github.thomasfischl.aihome.brain;

public class NetworkTest {

  public static void main(String[] args) {
    Brain brain = new Brain();
    double[] result = brain.compute(new double[] { 0, 0, 0, 0, 0, 0, 0, 0.08, 0.15, 0.15, 0, 0.08, 0.23, 0.23, 0.31, 0.23 });
    for (double val : result) {
      System.out.format("%.2f ", val);
    }
    System.out.println();
  }

}
