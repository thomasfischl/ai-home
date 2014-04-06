package com.github.thomasfischl.aihome.game2048controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GameController {

  private WebDriver driver;
  private String url;
  private GameGrid grid = new GameGrid(4);

  public GameController(String url) {
    System.setProperty("webdriver.chrome.driver", "./tool/chromedriver.exe");
    this.url = url;
  }

  public void start() {
    driver = new ChromeDriver();
    driver.get(url);
    sleep(2, TimeUnit.SECONDS);
  }

  private void sleep(long time, TimeUnit unit) {
    try {
      Thread.sleep(unit.toMillis(time));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void fireUpEvent() {
    fireEvent(Keys.ARROW_UP);
  }

  public void fireDownEvent() {
    fireEvent(Keys.ARROW_DOWN);
  }

  public void fireLeftEvent() {
    fireEvent(Keys.ARROW_LEFT);
  }

  public void fireRightEvent() {
    fireEvent(Keys.ARROW_RIGHT);
  }

  private void fireEvent(Keys key) {
    WebElement body = driver.findElement(By.tagName("body"));
    body.sendKeys(key);
  }

  public GameGrid getGrid() {
    grid.clear();

    int errorCount = 0;

    while (errorCount < 3) {
      try {
        List<WebElement> tiles = driver.findElements(By.className("tile"));
        for (WebElement tile : tiles) {
          if (tile.getTagName().equalsIgnoreCase("div")) {
            String classNames = tile.getAttribute("class");
            String[] parts = classNames.split(" ");

            int row = -1;
            int col = -1;
            int val = -1;

            for (String part : parts) {
//              System.out.println(part);
              if (part.startsWith("tile-position-")) {
                col = Integer.parseInt(part.substring(14, 15));
                row = Integer.parseInt(part.substring(16, 17));
              } else if (part.startsWith("tile-new")) {
                // new tile
              } else if (part.startsWith("tile-merged")) {
                // new merged
              } else if (part.startsWith("tile-")) {
                val = Integer.parseInt(part.substring(5));
              }
            }

            if (row != -1 && col != -1 && val != -1) {
              grid.setCell(col - 1, row - 1, val);
            }
          }
        }
        break;
      } catch (StaleElementReferenceException e) {
        errorCount++;
        System.out.println(e);
      }
    }
    return grid;
  }

  public void stop() {
    driver.close();
  }

}
