package com.github.thomasfischl.aihome.controller.pi;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PiMicroControllerTest {

  public static void main(String[] args) throws Exception {
    System.out.println("<--Pi4J--> GPIO Control Example ... started.");

    final PiMicroController controller = new PiMicroController();

    for (BluetoothDevice dev : controller.getAllBluetoothDevices()) {
      System.out.println("Device: " + dev);
    }

    ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
    pool.scheduleAtFixedRate(new Runnable() {
      int state = 0;

      @Override
      public void run() {
        System.out.println("");
        if (controller.isButtonPressed()) {
          switch (state) {
          case 0:
            controller.setGreenLedState(true);
            controller.setYellowLedState(false);
            controller.setRedLedState(false);
            break;
          case 1:
            controller.setGreenLedState(true);
            controller.setYellowLedState(true);
            controller.setRedLedState(false);
            break;
          case 2:
            controller.setGreenLedState(true);
            controller.setYellowLedState(true);
            controller.setRedLedState(true);
            break;
            
          default:
            controller.setGreenLedState(false);
            controller.setYellowLedState(false);
            controller.setRedLedState(false);
            break;
          }
          
          state++;
        }

      }
    }, 0, 10, TimeUnit.MILLISECONDS);

    System.out.println("Press any key ...");
    System.in.read();
  }
}
