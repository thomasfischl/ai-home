package com.github.thomasfischl.aihome.controller;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ImportResource({ "/com/github/thomasfischl/aihome/controller/integration.xml", "sensor-config.xml" })
@ComponentScan
@EnableScheduling
public class Application {

  @SuppressWarnings("resource")
  public static void main(String[] args) {
    new AnnotationConfigApplicationContext(Application.class);
  }

}
