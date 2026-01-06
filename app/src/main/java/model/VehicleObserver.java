package model;

/**
 * An interface to set up onVehicleUpdate.
 */
public interface VehicleObserver {

  /**
   * Implemented in Vehicle.java
   * Retrieves the current vehicle information sends the information to the visualization module.
   *
   * @return boolean for if the trip is complete.
   */
  public boolean provideInfo();

  public void setVehicleSubject(VehicleConcreteSubject vehicleConcreteSubject);
}
