package com.github.thomasfischl.aihome.controller;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Application {

  public static void main(String[] args) {
    GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
    ctx.setValidating(false);
    ctx.load("com/github/thomasfischl/aihome/controller/integration.xml");
    ctx.load("sensor-config.xml");
    ctx.refresh();
    ctx.start();
  }

}
