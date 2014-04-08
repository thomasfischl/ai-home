package com.github.thomasfischl.aihome.game2048controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;
import com.github.thomasfischl.aihome.game2048controller.controller.IGameController;
import com.github.thomasfischl.aihome.game2048controller.controller.java.JavaGameController;
import com.github.thomasfischl.aihome.game2048controller.training.TrainingDataCollector;

public class GameControllerMain {

  private static IGameController controller;
  private static TrainingDataCollector trainer;

  public static void main(String[] args) {
    trainer = new TrainingDataCollector(new File("./traindata/manuel"));
    trainer.start();

    controller = new JavaGameController();
    // controller = new SeleniumGameController("http://gabrielecirulli.github.io/2048/");
    controller.start();

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  private static void createAndShowGUI() {
    JFrame frame = new JFrame("Game 2048");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(10, 10, 100, 100);

    frame.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        GameGrid grid = controller.getGrid();
        Direction key = null;

        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          key = Direction.LEFT;
          break;
        case KeyEvent.VK_RIGHT:
          key = Direction.RIGHT;
          break;
        case KeyEvent.VK_UP:
          key = Direction.UP;
          break;
        case KeyEvent.VK_DOWN:
          key = Direction.DOWN;
          break;
        default:
          System.out.println("keycode: " + e.getKeyCode());
          break;
        }

        if (key != null) {
          trainer.saveTrainingData(grid, key);
          controller.move(key);
        }

        System.out.println(controller.getGrid());
      }
    });

    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent arg0) {
        controller.stop();
        trainer.stop();
      }
    });
  }

}
