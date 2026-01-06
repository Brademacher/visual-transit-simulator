package model;

/**
 * An interface to set up vehicle Factory method.
 */
public interface VehicleFactory {
  /**
   * Creates a vehicle of the chosen type for a given line.
   *
   * @param line the line used for setting the vehicle field values.
   *
   * @return a new Vehicle object.
   */
  public Vehicle generateVehicle(Line line);

  /**
   * Method used to return a vehicle to the Storage Facility object.
   * Increments the value of the vehicle type that just completed its trip.
   *
   * @param vehicle the that completed its trip.
   */
  public void returnVehicle(VehicleInterface vehicle);
}
