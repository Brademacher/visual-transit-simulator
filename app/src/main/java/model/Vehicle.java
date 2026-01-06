package model;

import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a vehicle in the transit system.
 * Vehicles have an id, capacity, speed, passengers, and position.
 * Subclasses (Bus, Train) must implement movement and reporting logic.
 */
public abstract class Vehicle implements VehicleInterface {

  private int id;
  private int capacity;
  //the speed is in distance over a time unit
  private double speed;
  private PassengerLoader loader;
  private PassengerUnloader unloader;
  private List<Passenger> passengers;
  private String name;
  private Position position;
  private Line line;
  private double distanceRemaining;
  private Stop nextStop;
  private List<Integer> carbonEmissionHistory;
  private VehicleConcreteSubject vehicleConcreteSubject;
  private Color color;

  /**
   * Constructor for a vehicle.
   *
   * @param id       vehicle identifier
   * @param line     line
   * @param capacity vehicle capacity
   * @param speed    vehicle speed
   * @param loader   passenger loader for vehicle
   * @param unloader passenger unloader for vehicle
   */
  public Vehicle(int id, Line line, int capacity, double speed, PassengerLoader loader,
                 PassengerUnloader unloader) {
    this.id = id;
    this.capacity = capacity;
    this.speed = speed;
    this.loader = loader;
    this.unloader = unloader;
    this.passengers = new ArrayList<Passenger>();
    this.line = line;
    this.distanceRemaining = 0;
    this.nextStop = line.getOutboundRoute().getNextStop();
    setName(line.getOutboundRoute().getName() + id);
    setPosition(new Position(nextStop.getPosition().getLongitude(),
        nextStop.getPosition().getLatitude()));
    carbonEmissionHistory = new ArrayList<Integer>();
  }

  /**
   * Abstract method implemented by vehicle subclasses to report statistics.
   *
   * @param out stream for printing
   */
  public abstract void report(PrintStream out);

  /**
   * Checks if a vehicle has completed its trip.
   *
   * @return boolean showing if the vehicle has finished its routes.
   */
  public boolean isTripComplete() {
    return line.getOutboundRoute().isAtEnd() && line.getInboundRoute().isAtEnd();
  }

  /**
   * Loads a passenger onto a vehicle if it's not at capacity.
   *
   * @param newPassenger the passenger being added to the vehicle.
   * @return the number of passengers added.
   */
  public int loadPassenger(Passenger newPassenger) {
    return getPassengerLoader().loadPassenger(newPassenger, getCapacity(), getPassengers());
  }

  /**
   * Moves the vehicle on its route.
   */
  public void move() {
    //actually move
    double speed = updateDistance();
    if (!isTripComplete() && distanceRemaining <= 0) {
      //load & unload
      int passengersHandled = handleStop();
      if (passengersHandled >= 0) {
        // if we spent time unloading/loading
        // we don't get to count excess distance towards next stop
        distanceRemaining = 0;
      }
      //switch to next stop
      toNextStop();
    }

    // Get the correct route and early exit
    Route currentRoute = line.getOutboundRoute();
    if (line.getOutboundRoute().isAtEnd()) {
      if (line.getInboundRoute().isAtEnd()) {
        return;
      }
      currentRoute = line.getInboundRoute();
    }
    Stop prevStop = currentRoute.prevStop();
    Stop nextStop = currentRoute.getNextStop();
    double distanceBetween = currentRoute.getNextStopDistance();
    // the ratio shows us how far from the previous stop are we in a ratio from 0 to 1
    double ratio;
    // check if we are at the first stop
    if (distanceBetween - 0.00001 < 0) {
      ratio = 1;
    } else {
      ratio = distanceRemaining / distanceBetween;
      if (ratio < 0) {
        ratio = 0;
        distanceRemaining = 0;
      }
    }
    double newLongitude = nextStop.getPosition().getLongitude() * (1 - ratio)
        + prevStop.getPosition().getLongitude() * ratio;
    double newLatitude = nextStop.getPosition().getLatitude() * (1 - ratio)
        + prevStop.getPosition().getLatitude() * ratio;
    setPosition(new Position(newLongitude, newLatitude));
  }

  /**
   * Update the simulation state for this vehicle.
   */
  public void update() {
    // update passengers FIRST
    // new passengers will get "updated" when getting on the vehicle
    for (Passenger passenger : getPassengers()) {
      passenger.pasUpdate();
    }
    if (!line.isIssueExist()) {
      move();
    }
    carbonEmissionHistory.add(0, getCurrentCO2Emission());
  }

  /**
   * Unloads passengers from a vehicle if at their stop.
   *
   * @return the number of passengers unloaded.
   */
  private int unloadPassengers() {
    return getPassengerUnloader().unloadPassengers(getPassengers(), nextStop);
  }

  /**
   * Handles unloading and loading of passengers at stop.
   *
   * @return int for the total number of passengers unloaded + loaded.
   */
  private int handleStop() {
    int passengersHandled = 0;

    // unloading passengers
    passengersHandled += unloadPassengers();

    // loading passengers
    passengersHandled += nextStop.loadPassengers(this);

    // if passengers were unloaded or loaded, it means we made
    // a stop to do the unload/load operation. In this case, the
    // distance remaining to the stop is 0 because we are at the stop.
    // If no unload/load operation was made and the distance is negative,
    // this means that we did not stop and keep going further.
    if (passengersHandled != 0) {
      distanceRemaining = 0;
    }
    return passengersHandled;
  }

  /**
   * Sets the nextStop field and calculates distance to the next stop.
   */
  private void toNextStop() {
    //current stop
    currentRoute().nextStop();
    if (!isTripComplete()) {
      // it's important we call currentRoute() again,
      // as nextStop() may have caused it to change.
      nextStop = currentRoute().getNextStop();
      distanceRemaining +=
          currentRoute().getNextStopDistance();
      // note, if distanceRemaining was negative because we
      // had extra time left over, that extra time is
      // effectively counted towards the next stop
    } else {
      nextStop = null;
      distanceRemaining = 999;
    }
  }

  /**
   * Updates the distance remaining and returns the effective speed of the vehicle.
   * Vehicle does not move if speed is negative or at end of route.
   *
   * @return double representing vehicle speed.
   */
  private double updateDistance() {

    if (isTripComplete()) {
      return 0;
    }
    if (getSpeed() < 0) {
      return 0;
    }
    distanceRemaining -= getSpeed();
    return getSpeed();
  }

  /**
   * Determine if vehicle is on the outgoing or incoming route.
   *
   * @return which route the vehicle is on.
   */
  private Route currentRoute() {

    if (!line.getOutboundRoute().isAtEnd()) {
      return line.getOutboundRoute();
    }
    return line.getInboundRoute();
  }

  /**
   * Retrieves the current vehicle information sends the information to the visualization module.
   *
   * @return whether the trip was completed
   */
  public boolean provideInfo() {
    boolean tripCompleted = false;
    if (!isTripComplete()) {
      JsonObject data = new JsonObject();
      data.addProperty("command", "observedVehicle");

      String type = getVehicleType();

      StringBuilder carbonEmissionHistoryString = new StringBuilder();
      int length = Math.min(5, carbonEmissionHistory.size());
      if (length > 0) {
        carbonEmissionHistoryString.append(carbonEmissionHistory.get(0));
        for (int i = 1; i < length; i++) {
          carbonEmissionHistoryString.append(", ");
          carbonEmissionHistoryString.append(carbonEmissionHistory.get(i));
        }
      }

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.format("%d", getId()) + System.lineSeparator());
      stringBuilder.append("-----------------------------" + System.lineSeparator());
      stringBuilder.append(String.format("* Type: %s", type) + System.lineSeparator());
      stringBuilder.append(String.format("* Position: (%f,%f)", getPosition().getLongitude(),
          getPosition().getLatitude()) + System.lineSeparator());
      stringBuilder.append(String.format("* Passengers: %d", getPassengers().size())
          + System.lineSeparator());
      stringBuilder.append(String.format("* CO2: %s", carbonEmissionHistoryString.toString())
          + System.lineSeparator());

      data.addProperty("text", stringBuilder.toString());

      vehicleConcreteSubject.getSession().sendJson(data);

      tripCompleted = false;
      return tripCompleted;
    } else {
      JsonObject data = new JsonObject();
      data.addProperty("command", "observedVehicle");
      data.addProperty("text", "");

      vehicleConcreteSubject.getSession().sendJson(data);

      tripCompleted = true;
      return tripCompleted;
    }
  }

  public abstract int getCurrentCO2Emission();

  public int getId() {
    return id;
  }

  public int getCapacity() {
    return capacity;
  }

  public double getSpeed() {
    return speed;
  }

  public PassengerLoader getPassengerLoader() {
    return loader;
  }

  public PassengerUnloader getPassengerUnloader() {
    return unloader;
  }

  public List<Passenger> getPassengers() {
    return passengers;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public void setVehicleSubject(VehicleConcreteSubject vehicleConcreteSubject) {
    this.vehicleConcreteSubject = vehicleConcreteSubject;
  }

  public Stop getNextStop() {
    return nextStop;
  }

  public Line getLine() {
    return line;
  }

  public double getDistanceRemaining() {
    return distanceRemaining;
  }
}
