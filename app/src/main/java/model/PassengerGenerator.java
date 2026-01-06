package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a generator for passengers at stops.
 */
public abstract class PassengerGenerator {
  private List<Stop> stops;
  private List<Double> probabilities;

  /**
   * Constructor for abstract class.
   *
   * @param stops         list of stops
   * @param probabilities list of probabilities
   */
  public PassengerGenerator(List<Stop> stops, List<Double> probabilities) {
    this.probabilities = new ArrayList<>();
    this.stops = new ArrayList<>();
    for (Stop s : stops) {
      this.stops.add(s);
    }
    for (Double probability : probabilities) {
      this.probabilities.add(probability);
    }
  }

  /**
   * Implemented by RandomPassengerGenerator to create passengers.
   * Passengers are generated at stops based on probability.
   *
   * @return the number of passengers generated.
   */
  public abstract int generatePassengers();

  /**
   * Gets a list of all stops on a route.
   *
   * @return a list of stops.
   */
  public List<Stop> getStops() {
    return stops;
  }

  public List<Double> getProbabilities() {
    return probabilities;
  }

}