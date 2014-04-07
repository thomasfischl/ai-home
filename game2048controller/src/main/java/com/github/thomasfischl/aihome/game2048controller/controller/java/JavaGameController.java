package com.github.thomasfischl.aihome.game2048controller.controller.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.github.thomasfischl.aihome.game2048controller.controller.AbstractGameController;
import com.github.thomasfischl.aihome.game2048controller.controller.Direction;
import com.github.thomasfischl.aihome.game2048controller.controller.GameGrid;

public class JavaGameController extends AbstractGameController {

  private List<Location> locations = new ArrayList<>();
  private Map<Location, Tile> gameGrid;
  final Set<Tile> mergedToBeRemoved = new HashSet<>();
  private List<Integer> traversalX = new ArrayList<Integer>();
  private List<Integer> traversalY = new ArrayList<Integer>();

  public JavaGameController() {
    super(4);
  }

  @Override
  public void start() {
    this.gameGrid = new HashMap<>();

    for (int i = 0; i < getDimension(); i++) {
      traversalX.add(i);
      traversalY.add(i);
    }

    for (int i = 0; i < getDimension(); i++) {
      for (int j = 0; j < getDimension(); j++) {
        Location loc = new Location(i, j);
        locations.add(loc);
      }
    }

    Tile tile0 = Tile.newRandomTile();
    List<Location> randomLocs = new ArrayList<>(locations);
    Collections.shuffle(randomLocs);
    Iterator<Location> locs = randomLocs.iterator();
    tile0.setLocation(locs.next());

    Tile tile1 = null;
    if (new Random().nextFloat() <= 0.8) { // gives 80% chance to add a
      // second tile
      tile1 = Tile.newRandomTile();
      if (tile1.getValue() == 4 && tile0.getValue() == 4) {
        tile1 = Tile.newTile(2);
      }
      tile1.setLocation(locs.next());
    }

    if (tile0 != null) {
      gameGrid.put(tile0.getLocation(), tile0);
    }
    if (tile1 != null) {
      gameGrid.put(tile1.getLocation(), tile1);
    }
  }

  @Override
  public void stop() {
    // nothing to do
  }

  @Override
  public GameGrid getGrid() {
    GameGrid grid = new GameGrid(getDimension());
    for (Tile tile : gameGrid.values()) {
      if (tile != null) {
        grid.setCell(tile.getLocation().getX(), tile.getLocation().getY(), tile.getValue());
      }
    }
    return grid;
  }

  @Override
  public void move(final Direction direction) {

    sortAndTraverseGrid(direction, new IntFunction() {
      @Override
      public int applyAsInt(int x, int y) {

        Location thisloc = new Location(x, y);
        Tile tile = gameGrid.get(thisloc);
        if (tile == null) {
          return 0;
        }

        Location farthestLocation = findFarthestLocation(thisloc, direction);
        Location nextLocation = farthestLocation.offset(direction);
        Tile tileToBeMerged = nextLocation.isValidFor(getDimension()) ? gameGrid.get(nextLocation) : null;

        if (tileToBeMerged != null && tileToBeMerged.getValue().equals(tile.getValue()) && !tileToBeMerged.isMerged()) {
          tileToBeMerged.merge(tile);

          gameGrid.put(nextLocation, tileToBeMerged);
          gameGrid.remove(tile.getLocation());
          gameGrid.put(tile.getLocation(), null);
          mergedToBeRemoved.add(tile);

        } else if (farthestLocation.equals(tile.getLocation()) == false) {
          gameGrid.put(farthestLocation, tile);
          gameGrid.remove(tile.getLocation());
          gameGrid.put(tile.getLocation(), null);
          tile.setLocation(farthestLocation);
        }
        return 0;
      }
    });

    addRandomTile();

    mergedToBeRemoved.clear();

    for (Tile t : gameGrid.values()) {
      if (t != null) {
        t.clearMerge();
      }
    }
  }

  private Location findFarthestLocation(Location location, Direction direction) {
    Location farthest;

    do {
      farthest = location;
      location = farthest.offset(direction);
    } while (location.isValidFor(getDimension()) && gameGrid.get(location) == null);

    return farthest;
  }

  private void sortAndTraverseGrid(Direction d, IntFunction func) {
    if (d.getX() == 1) {
      Collections.sort(traversalX, Collections.reverseOrder());
    } else {
      Collections.sort(traversalX);
    }

    if (d.getY() == 1) {
      Collections.sort(traversalY, Collections.reverseOrder());
    } else {
      Collections.sort(traversalY);
    }

    for (Integer t_x : traversalX) {
      for (Integer t_y : traversalY) {
        func.applyAsInt(t_x, t_y);
      }
    }
  }

  private void addRandomTile() {
    List<Location> availableLocations = new ArrayList<Location>();
    for (Location l : locations) {
      if (gameGrid.get(l) == null) {
        availableLocations.add(l);
      }
    }

    Collections.shuffle(availableLocations);

    if (availableLocations.size() > 0) {
      Location randomLocation = availableLocations.get(new Random().nextInt(availableLocations.size()));
      Tile tile = Tile.newRandomTile();
      tile.setLocation(randomLocation);
      gameGrid.put(tile.getLocation(), tile);
    }
  }

  private interface IntFunction {
    int applyAsInt(int left, int right);
  }

}
