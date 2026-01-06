package model;

/**
 * Represents a train vehicle in the transit simulation.
 * Trains travel along a Line and can carry a large number of passengers
 * between stops at a fixed speed.
 */
public abstract class Train extends Vehicle {

  /** Constant string identifier for train-type vehicles. */
  public static final String TRAIN_VEHICLE = "TRAIN_VEHICLE";

  /**
   * Constructor for a train.
   *
   * @param id       train identifier
   * @param line     route of in/out bound
   * @param capacity capacity of the train
   * @param speed    speed of the train
   */
  public Train(int id, Line line, int capacity, double speed) {
    super(id, line, capacity, speed, new PassengerLoader(), new PassengerUnloader());
  }
}
