package com.github.thomasfischl.aihome.game2048controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.thomasfischl.aihome.brain.Brain;
import com.github.thomasfischl.aihome.brain.NormalizerGame2048;
import com.github.thomasfischl.aihome.game2048controller.GameController.KeyCode;

public class GameTrainerMain {

  private static GameController controller;
  private static GameTrainer trainer;

  private static Brain brain;
  private static NormalizerGame2048 normalizer = new NormalizerGame2048();
  private static ScheduledExecutorService pool;

  public static void main(String[] args) {
    trainer = new GameTrainer(new File("./traindata"));
    brain = new Brain();

    pool = Executors.newScheduledThreadPool(1);

    controller = new GameController("http://gabrielecirulli.github.io/2048/");
    controller.start();

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  private static void createAndShowGUI() {

    // Create and set up the window.
    JFrame frame = new JFrame("ButtonDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(10, 10, 100, 100);
    frame.setBackground(Color.red);

    frame.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        GameGrid grid = controller.getGrid();
        KeyCode key = null;

        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          key = KeyCode.LEFT;
          break;
        case KeyEvent.VK_RIGHT:
          key = KeyCode.RIGHT;
          break;
        case KeyEvent.VK_UP:
          key = KeyCode.UP;
          break;
        case KeyEvent.VK_DOWN:
          key = KeyCode.DOWN;
          break;
        default:
          System.out.println("keycode: " + e.getKeyCode());
          break;
        }

        if (key != null) {
          trainer.saveTrainingData(grid, key);
          controller.fire(key);
        }

        System.out.println(controller.getGrid());
        calculateNextStep(controller.getGrid());
      }
    });

    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent arg0) {
        controller.stop();
        trainer.stop();
        pool.shutdownNow();
      }
    });

    JButton btnBrain = new JButton();
    btnBrain.setText("Brain");
    btnBrain.setSize(200, 20);
    btnBrain.addActionListener(new ActionListener() {
      private Future<?> runnableResult;

      @Override
      public void actionPerformed(ActionEvent arg0) {
        if (runnableResult == null) {
          runnableResult = pool.submit(new BrainTask());
        } else {
          runnableResult.cancel(true);
          runnableResult = null;
        }
      }
    });

    frame.add(btnBrain);

  }

  public static KeyCode calculateNextStep(GameGrid grid) {

    double[] data = new double[16];
    int idx = 0;
    for (int i = 0; i < grid.getDimension(); i++) {
      for (int j = 0; j < grid.getDimension(); j++) {
        double val = normalizer.map(grid.getCell(j, i));
        data[idx++] = val;
      }
    }

    double[] result = brain.compute(data);

    double max = -1;
    KeyCode key = KeyCode.LEFT;

    if (result[0] > max) {
      key = KeyCode.DOWN;
      max = result[0];
    }

    if (result[1] > max) {
      key = KeyCode.UP;
      max = result[1];
    }

    if (result[2] > max) {
      key = KeyCode.LEFT;
      max = result[2];
    }

    if (result[3] > max) {
      key = KeyCode.RIGHT;
      max = result[3];
    }
    System.out.println("Next move: " + key);

    return key;

  }

  private static final class BrainTask implements Runnable {
    private Random rand = new Random();

    @Override
    public void run() {
      GameGrid lastGrid = null;
      while (true) {
        GameGrid grid = controller.getGrid();

        KeyCode key;
        if (grid.equals(lastGrid)) {
          key = KeyCode.values()[rand.nextInt(4)];
          System.out.println("Random move");
        } else {
          key = calculateNextStep(grid);
        }

        controller.fire(key);
        lastGrid = grid;
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
          break;
        }
      }
    }
  }
}
