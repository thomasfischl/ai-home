package com.github.thomasfischl.aihome.controller.pi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class PiMicroController {

  private static PiMicroController singleton;

  private GpioController gpioController;
  private GpioPinDigitalOutput greenLed;
  private GpioPinDigitalOutput yellowLed;
  private GpioPinDigitalOutput redLed;
  private GpioPinDigitalOutput relay;
  private GpioPinDigitalInput button;

  private boolean greenLedState;
  private boolean yellowLedState;
  private boolean redLedState;
  private boolean relayState;
  private boolean buttonPressed;

  private PiMicroController() {
    gpioController = GpioFactory.getInstance();
    greenLed = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Led01 (green)", PinState.LOW);
    yellowLed = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Led02 (yellow)", PinState.LOW);
    redLed = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Led03 (red)", PinState.LOW);
    relay = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Relay01", PinState.LOW);
    button = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);

    button.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // System.out.println("--> button push - " + event.getEventType() + " " + event.getPin() + " " + event.getState());
        if (event.getState() == PinState.HIGH) {
          buttonPressed = true;
        }
      }
    });

  }

  public List<BluetoothDevice> getAllBluetoothDevices() throws IOException {
    List<BluetoothDevice> devices = new ArrayList<>();
    Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", "hcitool scan --flush" });
    try {
      p.waitFor();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }

    List<String> data = IOUtils.readLines(p.getInputStream());
    if (data != null) {
      for (String line : data) {
        line = line.trim();
        if (line.charAt(2) == ':') {
          devices.add(new BluetoothDevice(null, line.substring(0, 17)));
          throw new RuntimeException("Set corret device name");
        }
      }
    }

    return devices;
  }

  public boolean isGreenLedOn() {
    return greenLedState;
  }

  public boolean isRedLedOn() {
    return redLedState;
  }

  public boolean isYellowLedOn() {
    return yellowLedState;
  }

  public boolean isRelayOn() {
    return relayState;
  }

  public boolean isButtonPressed() {
    if (buttonPressed) {
      buttonPressed = false;
      return true;
    }
    return false;
  }

  public void setGreenLedState(boolean state) {
    greenLedState = state;
    greenLed.setState(greenLedState);
  }

  public void setYellowLedState(boolean state) {
    yellowLedState = state;
    yellowLed.setState(yellowLedState);
  }

  public void setRedLedState(boolean state) {
    redLedState = state;
    redLed.setState(redLedState);
  }

  public void setRelayState(boolean state) {
    relayState = state;
    relay.setState(relayState);
  }

  public static PiMicroController getInstance() {
    if (singleton == null) {
      singleton = new PiMicroController();
    }
    return singleton;
  }

}
