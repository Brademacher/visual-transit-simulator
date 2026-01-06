package model;

/**
 * Interface used by day/night strategies to choose the right vehicle to use.
 */
public interface GenerationStrategy {

  /**
   * Chooses the right vehicle type to use.
   *
   * @param storageFacility the storage facility object holding the count for each vehicle type.
   * @return a string for the type of vehicle to use.
   */
  public String getTypeOfVehicle(StorageFacility storageFacility);
}
