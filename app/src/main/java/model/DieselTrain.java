package model;

import java.io.PrintStream;

/**
 * Represents a Diesel Train in the transit system.
 */
public class DieselTrain extends Train {
  /** Used for creating a diesel train json object. */
  public static final String DIESEL_TRAIN_VEHICLE = "DIESEL_TRAIN_VEHICLE";

  /** Used for setting the speed of a diesel train object. */
  public static final double SPEED = 1;

  /** Used for setting the passenger capacity of a diesel train object. */
  public static final int CAPACITY = 120;

  /**
   * Constructor for a train.
   *
   * @param id       train identifier
   * @param line     route of in/out bound
   * @param capacity capacity of the train
   * @param speed    speed of the train
   */
  public DieselTrain(int id, Line line, int capacity, double speed) {
    super(id, line, capacity, speed);
  }

  /**
   * Report statistics for the train.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Diesel Train Info Start####");
    out.println("ID: " + getId());
    out.println("Name: " + getName());
    out.println("Speed: " + getSpeed());
    out.println("Capacity: " + getCapacity());
    out.println("Position: " + (getPosition().getLatitude() + "," + getPosition().getLongitude()));
    out.println("Distance to next stop: " + getDistanceRemaining());
    out.println("****Passengers Info Start****");
    out.println("Num of passengers: " + getPassengers().size());
    for (Passenger pass : getPassengers()) {
      pass.report(out);
    }
    out.println("****Passengers Info End****");
    out.println("####Diesel Train Info End####");
  }

  /**
   * Co2 consumption for an electric train.
   *
   * @return The current co2 consumption value
   */
  public int getCurrentCO2Emission() {
    return ((3 * getPassengers().size()) + 6);
  }

  @Override
  public String getVehicleType() {
    return DIESEL_TRAIN_VEHICLE;
  }
}
