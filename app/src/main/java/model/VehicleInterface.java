package model;

import java.io.PrintStream;
import java.util.List;

public interface VehicleInterface extends VehicleObserver {

  /**
   * Moves the vehicle on its route.
   */
  void move();

  /**
   * Update the simulation state for this vehicle.
   */
  void update();

  /**
   * Checks if a vehicle has completed its trip.
   *
   * @return boolean showing if the vehicle has finished its routes.
   */
  boolean isTripComplete();

  /**
   * method implemented by vehicle subclasses to report statistics.
   * @param out stream for printing
   */
  void report(PrintStream out);

  /**
   * Loads a passenger onto a vehicle if it's not at capacity.
   *
   * @param newPassenger the passenger being added to the vehicle.
   * @return the number of passengers added.
   */
  int loadPassenger(Passenger newPassenger);

  /**
   * The list of passengers on a vehicle.
   *
   * @return list of passengers
   */
  List<Passenger> getPassengers();

  /**
   * Loads the C02 emisson of a vehicle.
   *
   * @return int of the C02 emission
   */
  int getCurrentCO2Emission();

  /**
  * Get the type of a vehicle.
  *
  * @return String of the vehicle type
  */
  String getVehicleType();

  /**
   * Loads in the id of a vehicle.
   *
   * @return int representing the id of a vehicle
   */
  int getId();

  /**
   * Loads in the speed of a vehicle.
   *
   * @return double representing the speed of a vehicle
   */
  double getSpeed();

  /**
   * Get the capacity of a vehicle.
   *
   * @return int representing the capacity of the vehicle
   */
  int getCapacity();

  /**
   * Get the name of the vehicle.
   *
   * @return String representing the name of the line
   */
  String getName();

  /**
   * Set the name of the vehicle.
   *
   * @param name the name that will be given to the vehicle
   */
  void setName(String name);

  /**
   * Get the line of a vehicle.
   *
   * @return Line of the vehicle
   */
  Line getLine();

  /**
   * Get the positon of the vehicle.
   *
   * @return Position of the vehicle
   */
  Position getPosition();

  /**
   * Set the position of the vehicle.
   * @param position the location of the where the vehicle will be
   */
  void setPosition(Position position);

  /**
   * Get the next stop.
   *
   * @return Stop that follows
   */
  Stop getNextStop();

  /**
   * Get the distance remaining in the route.
   *
   * @return double representing the distance that remains
   */
  double getDistanceRemaining();

  /**
   * Get the loader for the vehicle.
   *
   * @return PassengerLoader for the vehicle
   */
  PassengerLoader getPassengerLoader();

  /**
   * Get the unloader for the vehicle.
   *
   * @return unPasLoad for the vehicle
   */
  PassengerUnloader getPassengerUnloader();
}
