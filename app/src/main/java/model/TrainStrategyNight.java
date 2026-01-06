package model;

/**
 * Nighttime scheduler for trains.
 */
public class TrainStrategyNight implements GenerationStrategy {
  private int counter;

  /** Constructor method. */
  public TrainStrategyNight() {
    this.counter = 0;
  }

  /**
   * Implements Night strategy to use the right train type.
   *
   * @param storageFacility the storage facility object holding the count for each vehicle type.
   * @return a string of the desired train type.
   */
  @Override
  public String getTypeOfVehicle(StorageFacility storageFacility) {
    String typeOfVehicle = null;
    if (counter == 0) {
      if (storageFacility.getElectricTrainsNum() > 0) {
        typeOfVehicle = ElectricTrain.ELECTRIC_TRAIN_VEHICLE;
      }
    } else {
      if (storageFacility.getDieselTrainsNum() > 0) {
        typeOfVehicle = DieselTrain.DIESEL_TRAIN_VEHICLE;
      }
    }

    if (typeOfVehicle != null) {
      counter++;
      counter = counter % 2;
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
