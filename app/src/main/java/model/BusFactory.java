package model;

/**
 * An interface to set up bus Factory method.
 */
public class BusFactory implements VehicleFactory {
  private GenerationStrategy generationStrategy;
  private Counter counter;
  private StorageFacility storageFacility;

  /**
   * Constructor for bus factory that selects strategy based on time.
   *
   * @param storageFacility storage facility information
   * @param counter counter information
   * @param time time used to select strategy
   */
  public BusFactory(StorageFacility storageFacility, Counter counter, int time) {
    if (time >= 8 && time < 18) {
      generationStrategy = new BusStrategyDay();
    } else {
      generationStrategy = new BusStrategyNight();
    }
    this.storageFacility = storageFacility;
    this.counter = counter;
  }

  /**
   * Creates the correct bus type vehicle, given availability.
   * Availability is determined by checking the storage facility.
   *
   * @param line the line used for setting the vehicle field values.
   *
   * @return a new bus (large or small) for the line passed in.
   */
  @Override
  public Vehicle generateVehicle(Line line) {
    String typeOfVehicle = generationStrategy.getTypeOfVehicle(storageFacility);
    Vehicle generatedVehicle = null;
    if (typeOfVehicle != null && typeOfVehicle.equals(SmallBus.SMALL_BUS_VEHICLE)) {
      generatedVehicle = new SmallBus(counter.getSmallBusIdCounterAndIncrement(),
          line, SmallBus.CAPACITY, SmallBus.SPEED);
      storageFacility.decrementSmallBusesNum();
    } else if (typeOfVehicle != null && typeOfVehicle.equals(LargeBus.LARGE_BUS_VEHICLE)) {
      generatedVehicle = new LargeBus(counter.getLargeBusIdCounterAndIncrement(), line,
          LargeBus.CAPACITY, LargeBus.SPEED);
      storageFacility.decrementLargeBusesNum();
    }
    return generatedVehicle;
  }

  /**
   * Adds a vehicle back to the storage facility once its trip is completed.
   *
   * @param vehicle the bus that completed its trip.
   */
  @Override
  public void returnVehicle(VehicleInterface vehicle) {
    String type = vehicle.getVehicleType();

    if (type.equals(SmallBus.SMALL_BUS_VEHICLE)) {
      storageFacility.incrementSmallBusesNum();
    } else if (type.equals(LargeBus.LARGE_BUS_VEHICLE)) {
      storageFacility.incrementLargeBusesNum();
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
