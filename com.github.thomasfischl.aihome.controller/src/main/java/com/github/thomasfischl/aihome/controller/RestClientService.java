package com.github.thomasfischl.aihome.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

@Service("sensorDataService")
public class RestClientService {

  private FileWriter fw;

  public RestClientService() {
    System.out.println("init");
  }

  @PostConstruct
  public void init() throws IOException {
    System.out.println("Open sensordata file ...");
    File tmpData = new File("./sensorData/data.txt");
	tmpData.getParentFile().mkdirs();
    fw = new FileWriter(tmpData);
  }

  @PreDestroy
  public void destroy() throws IOException {
    System.out.println("Closing sensordata file ...");
    fw.close();
  }

  @ServiceActivator
  public void process(SensorDataGroup data) throws IOException {
    System.err.println(data);
    fw.write(data.toString() + "\n");
    fw.flush();

    try {
      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.postForObject("http://localhost:8080/sensordata/thomas", data, String.class);
      System.out.println("result: " + result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
