package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrainStrategyNightTest {

  /**
   * Setup deterministic operations before each test run.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test constructor normal.
   */
  @Test
  public void testConstructor() {
    TrainStrategyNight trainStrategyNight = new TrainStrategyNight();
    assertEquals(0, trainStrategyNight.getCounter());
  }

  /**
   * Testing to get correct vehicle according to the strategy.
   */
  @Test
  public void testGetTypeOfVehicle() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 1, 1);
    TrainStrategyNight trainStrategyDay = new TrainStrategyNight();
    String strToCmpr;
    for (int i = 0; i < 1; i++) {
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, strToCmpr);
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(DieselTrain.DIESEL_TRAIN_VEHICLE, strToCmpr);
    }
  }

  /**
   * Test getTypeOfVehicle when no electric trains are available.
   */
  @Test
  public void testGetTypeOfVehicleNoElectricTrains() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 0, 1); 
    TrainStrategyNight trainStrategyNight = new TrainStrategyNight();
    String result = trainStrategyNight.getTypeOfVehicle(storageFacility);
    assertNull(result);
    assertEquals(0, trainStrategyNight.getCounter()); 
  }

  /**
   * Test getTypeOfVehicle when no diesel trains are available.
   */
  @Test
  public void testGetTypeOfVehicleNoDieselTrains() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 1, 0); 
    TrainStrategyNight trainStrategyNight = new TrainStrategyNight();
    String result = trainStrategyNight.getTypeOfVehicle(storageFacility);
    assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, result);
    assertEquals(1, trainStrategyNight.getCounter()); 
    result = trainStrategyNight.getTypeOfVehicle(storageFacility);
    assertNull(result);
    assertEquals(1, trainStrategyNight.getCounter());
  }
}
