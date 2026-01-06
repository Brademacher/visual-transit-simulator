package model;

/**
 * Nighttime schedule for buses.
 */
public class BusStrategyNight implements GenerationStrategy {
  private int counter;

  /** Constructor method. */
  public BusStrategyNight() {
    this.counter = 0;
  }

  /**
   * Implements Night strategy to use the right bus type.
   *
   * @param storageFacility the storage facility object holding the count for each vehicle type.
   * @return a string of the desired bus type.
   */
  @Override
  public String getTypeOfVehicle(StorageFacility storageFacility) {
    String typeOfVehicle = null;
    if (counter < 3) {
      if (storageFacility.getSmallBusesNum() > 0) {
        typeOfVehicle = SmallBus.SMALL_BUS_VEHICLE;
      }
    } else {
      if (storageFacility.getLargeBusesNum() > 0) {
        typeOfVehicle = LargeBus.LARGE_BUS_VEHICLE;
      }
    }

    if (typeOfVehicle != null) {
      counter++;
      counter = counter % 4;
    }
    return typeOfVehicle;
  }

  /**
   * Gets current counter value.
   *
   * @return an int for counter value.
   */
  public int getCounter() {
    return counter;
  }
}
