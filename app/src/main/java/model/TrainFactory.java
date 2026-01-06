package model;

/**
 * An interface to set up train Factory method.
 */
public class TrainFactory implements VehicleFactory {
  private GenerationStrategy generationStrategy;
  private Counter counter;
  private StorageFacility storageFacility;

  /**
   * Initializes the strategy depending on time of day when simulation is started.
   *
   * @param storageFacility   storage facility information
   * @param counter           counter information
   * @param time              hour of the day
   */
  public TrainFactory(StorageFacility storageFacility, Counter counter, int time) {
    if (time >= 8 && time < 18) {
      generationStrategy = new TrainStrategyDay();
    } else {
      generationStrategy = new TrainStrategyNight();
    }
    this.storageFacility = storageFacility;
    this.counter = counter;
  }

  /**
   * Creates the correct train type vehicle, given availability.
   * Availability is determined by checking the storage facility.
   *
   * @param line the line used for setting the vehicle field values.
   *
   * @return a new train (diesel or electric) for the line passed in.
   */
  @Override
  public Vehicle generateVehicle(Line line) {
    String typeOfVehicle = generationStrategy.getTypeOfVehicle(storageFacility);
    Vehicle generatedVehicle = null;

    if (typeOfVehicle != null && typeOfVehicle.equals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE)) {
      generatedVehicle = new ElectricTrain(counter.getElectricTrainIdCounterAndIncrement(),
          line, ElectricTrain.CAPACITY,
          ElectricTrain.SPEED);
      storageFacility.decrementElectricTrainsNum();
    } else if (typeOfVehicle != null && typeOfVehicle.equals(DieselTrain.DIESEL_TRAIN_VEHICLE)) {
      generatedVehicle = new DieselTrain(counter.getDieselTrainIdCounterAndIncrement(),
          line, DieselTrain.CAPACITY,
          DieselTrain.SPEED);
      storageFacility.decrementDieselTrainsNum();
    }

    return generatedVehicle;
  }

  /**
   * Adds a train back to the storage facility once its trip is completed.
   *
   * @param vehicle the train that completed its trip.
   */
  @Override
  public void returnVehicle(VehicleInterface vehicle) {
    String type = vehicle.getVehicleType();

    if (type.equals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE)) {
      storageFacility.incrementElectricTrainsNum();
    } else if (type.equals(DieselTrain.DIESEL_TRAIN_VEHICLE)) {
      storageFacility.incrementDieselTrainsNum();
    }
  }

  /**
   * Get storage facility.
   *
   * @return storage facility
   */
  public StorageFacility getStorageFacility() {
    return storageFacility;
  }

  /**
   * Get generation strategy.
   *
   * @return generation strategy
   */
  public GenerationStrategy getGenerationStrategy() {
    return generationStrategy;
  }

}
