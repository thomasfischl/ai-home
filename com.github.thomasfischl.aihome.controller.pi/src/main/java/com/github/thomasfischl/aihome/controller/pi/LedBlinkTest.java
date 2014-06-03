package com.github.thomasfischl.aihome.controller.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class LedBlinkTest {

  public static void main(String[] args) throws Exception {
    System.out.println("<--Pi4J--> GPIO Control Example ... started.");

    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput ledPin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Led01 (green)", PinState.LOW);
    final GpioPinDigitalOutput ledPin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Led02 (yellow)", PinState.LOW);
    final GpioPinDigitalOutput ledPin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Led03 (red)", PinState.LOW);
    final GpioPinDigitalOutput relayPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Relay01", PinState.LOW);

    final GpioPinDigitalInput buttonPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);

    ledPin1.low();
    ledPin2.low();
    ledPin3.low();
    relayPin.low();

    buttonPin.addListener(new GpioPinListenerDigital() {
      int state = 0;

      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        System.out.println("--> button push - " + event.getEventType() + " " + event.getPin() + " " + event.getState());
        if (event.getState() == PinState.HIGH) {
          System.out.println("State: " + state);
          if (state == 0) {
            ledPin1.high();
          }
          if (state == 1) {
            ledPin2.high();
          }
          if (state == 2) {
            ledPin3.high();
          }
          if (state == 3) {
            relayPin.high();
          }
          if (state == 4) {
            state = -1;
            ledPin1.low();
            ledPin2.low();
            ledPin3.low();
            relayPin.low();
          }
          state++;
        }
      }
    });

    System.out.println("Press any key ...");
    System.in.read();

    gpio.shutdown();
  }
}
