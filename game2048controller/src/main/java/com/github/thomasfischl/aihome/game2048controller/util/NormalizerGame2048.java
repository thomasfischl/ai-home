package com.github.thomasfischl.aihome.game2048controller.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NormalizerGame2048 {

  private Map<Integer, Double> mapping = new HashMap<>();

  public NormalizerGame2048() {
    mapping.put(0, 0.0);
    for (int i = 0; i < 12; i++) {
      mapping.put((int) Math.pow(2, i), (1.0 / 13.0) * (i + 1));
    }

    // for (Entry<Integer, Double> entry : mapping.entrySet()) {
    // System.out.println(entry.getKey() + ": " + entry.getValue());
    // }
  }

  public double map(int val) {
    return mapping.get(val);
  }

  public String mapAsString(int val) {
    return String.format(Locale.US, "%.2f", map(val));
  }

}
