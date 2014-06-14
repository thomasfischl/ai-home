package com.github.thomasfischl.aihome.game2048controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.controller.java.JavaGameController;

public class GameAIControllerFullTestMain {

  private static final int ITERATION_COUNT = 1000;
  private static ExecutorService pool;

  public static void main(String[] args) throws Exception {
    pool = Executors.newFixedThreadPool(10);

    List<Future<GameGrid>> results = new ArrayList<>();
    for (int i = 0; i < ITERATION_COUNT; i++) {
      Brain brain = new Brain();
      IGameController controller = new JavaGameController();
      controller.start();
      Future<GameGrid> result = pool.submit(new GameBrainTask(controller, brain, 0, false) {
        @Override
        protected void reportResult() {
        }
      });
      results.add(result);
    }

    int score = 0;
    int count = 0;
    Map<Integer, AtomicInteger> historgram = new HashMap<Integer, AtomicInteger>();
    for (Future<GameGrid> result : results) {
      if (!historgram.containsKey(result.get().highNumber())) {
        historgram.put(result.get().highNumber(), new AtomicInteger(1));
      } else {
        historgram.get(result.get().highNumber()).incrementAndGet();
      }
      count++;
      score += result.get().score();
      if (count % 100 == 0) {
        System.out.println("Count: " + count);
      }
    }

    List<Integer> keys = new ArrayList<Integer>(historgram.keySet());
    Collections.sort(keys);
    for (Integer key : keys) {
      System.out.println(key + ": " + historgram.get(key).intValue());
    }
    System.out.println("Score: " + (score / count));

    pool.shutdown();
  }

}
