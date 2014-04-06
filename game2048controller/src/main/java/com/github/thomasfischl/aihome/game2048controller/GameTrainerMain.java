package com.github.thomasfischl.aihome.game2048controller;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameTrainerMain {

  private static GameController controller;

  public static void main(String[] args) {
    controller = new GameController("http://gabrielecirulli.github.io/2048/");
    controller.start();

    GameGrid grid = controller.getGrid();
    System.out.println(grid);

    // controller.stop();

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
        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          controller.fireLeftEvent();
          break;
        case KeyEvent.VK_RIGHT:
          controller.fireRightEvent();
          break;
        case KeyEvent.VK_UP:
          controller.fireUpEvent();
          break;
        case KeyEvent.VK_DOWN:
          controller.fireDownEvent();
          break;
        default:
          System.out.println("keycode: " + e.getKeyCode());
          break;
        }

        System.out.println(controller.getGrid());

      }
    });

    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent arg0) {
        controller.stop();
      }
    });

  }

}
