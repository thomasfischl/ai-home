package com.github.thomasfischl.aihome.server;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataGroup;

@RestController()
@RequestMapping("/sensordata")
public class SeansorDataRestService {

  // @RequestMapping(method = RequestMethod.GET, value = "/{user}")
  @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/{user}")
  @ResponseBody
  public String process(@PathVariable String user, @RequestBody(required = false) SensorDataGroup group) {
    System.out.println("request new sensor data from user '" + user + "'");

    if (group != null && !group.getValues().isEmpty()) {
      System.out.println("Sensor data count: " + group.getValues().size());
    } else {
      System.out.println("the request contains no sensor data.");
    }

    return user;
  }
}