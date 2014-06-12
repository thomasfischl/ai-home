package com.github.thomasfischl.aihome.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class PidFileWatcher implements Runnable {

  private File pidFile = new File("/tmp/controller.pid");

  public PidFileWatcher() {
    try {
      FileUtils.touch(pidFile);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  @Override
  public void run() {
    if (!pidFile.exists()) {
      System.out.println("PID file doesn't exists.");
      System.out.println("Shutdown AI-Home Controller ...");
      System.exit(0);
    }
  }

}
