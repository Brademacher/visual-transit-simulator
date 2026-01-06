package model;

/**
 * A counter with unique identifiers for various objects in simulation system.
 */
public class Counter {

  private int routeIdCounter = 10;
  private int stopIdCounter = 100;
  private int smallBusIdCounter = 1000;
  private int largeBusIdCounter = 2000;
  private int electricTrainIdCounter = 3000;
  private int dieselTrainIdCounter = 4000;
  private int lineIdCounter = 10000;

  /** Constructor for Counter. */
  public Counter() {

  }

  /**
   * Increments the route ID counter.
   *
   * @return the current route ID int + 1.
   */
  public int getRouteIdCounterAndIncrement() {
    return routeIdCounter++;
  }

  /**
   * Increments the stop ID counter.
   *
   * @return the current stop ID int + 1.
   */
  public int getStopIdCounterAndIncrement() {
    return stopIdCounter++;
  }

  /**
   * Increments the small bus ID counter.
   *
   * @return the current small bus ID int + 1.
   */
  public int getSmallBusIdCounterAndIncrement() {
    return smallBusIdCounter++;
  }

  /**
   * Increments the large bus ID counter.
   *
   * @return the current large bus ID int + 1.
   */
  public int getLargeBusIdCounterAndIncrement() {
    return largeBusIdCounter++;
  }

  /**
   * Increments the electric train ID counter.
   *
   * @return the current electric train ID int + 1.
   */
  public int getElectricTrainIdCounterAndIncrement() {
    return electricTrainIdCounter++;
  }

  /**
   * Increments the diesel train ID counter.
   *
   * @return the current diesel train ID int + 1.
   */
  public int getDieselTrainIdCounterAndIncrement() {
    return dieselTrainIdCounter++;
  }

  /**
   * Increments the line ID counter.
   *
   * @return the current line ID int + 1.
   */
  public int getLineIdCounterAndIncrement() {
    return lineIdCounter++;
  }

}
