package model;

import java.io.PrintStream;

/**
 * Represents a passenger in the transit simulation.
 * Each passenger has a name, a destination stop, and tracks
 * how long they have waited and been on a vehicle.
 */
public class Passenger {

  private String name;
  private int destinationStopId;
  private int waitAtStop;
  private int timeOnVehicle;

  /**
   * Constructor for passenger.
   *
   * @param name              name of passenger
   * @param destinationStopId destination stop id
   */
  public Passenger(int destinationStopId, String name) {
    this.name = name;
    this.destinationStopId = destinationStopId;
    this.waitAtStop = 0;
    this.timeOnVehicle = 0;
  }

  /**
   * Updates time variables for passenger.
   */
  public void pasUpdate() {
    if (isOnVehicle()) {
      timeOnVehicle++;
    } else {
      waitAtStop++;
    }
  }

  /**
   * Checks if the passenger is on a vehicle of any kind.
   *
   * @return boolean value: 1 for true, 0 for false.
   */
  public boolean isOnVehicle() {
    return timeOnVehicle > 0;
  }

  /**
   * Report statistics for passenger.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Passenger Info Start####");
    out.println("Name: " + name);
    out.println("Destination: " + destinationStopId);
    out.println("Wait at stop: " + waitAtStop);
    out.println("Time on vehicle: " + timeOnVehicle);
    out.println("####Passenger Info End####");
  }

  /**
   * Gets the number of steps passenger waiting at stop.
   *
   * @return int for number of steps waiting/waited at stop.
   */
  public int getWaitAtStop() {
    return waitAtStop;
  }

  /**
   * Gets time (number of steps) passenger on vehicle.
   *
   * @return int for number of steps on vehicle.
   */
  public int getTimeOnVehicle() {
    return timeOnVehicle;
  }

  /**
   * Gets passenger destination stop ID.
   *
   * @return int for destination stop ID.
   */
  public int getDestination() {
    return destinationStopId;
  }

  /** Sets the passenger onto a vehicle. */
  public void setOnVehicle() {
    timeOnVehicle = 1;
  }
}
