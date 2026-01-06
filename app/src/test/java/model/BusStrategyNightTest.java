package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusStrategyNightTest {

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
    BusStrategyNight busStrategyNight = new BusStrategyNight();
    assertEquals(0, busStrategyNight.getCounter());
  }

  /**
   * Testing to get correct vehicle according to the strategy.
   */
  @Test
  public void testGetTypeOfVehicle() {
    StorageFacility storageFacility = new StorageFacility(3, 1, 0, 0);
    BusStrategyNight busStrategyDay = new BusStrategyNight();
    String strToCmpr;
    for (int i = 0; i < 1; i++) {
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, strToCmpr);
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, strToCmpr);
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, strToCmpr);
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(LargeBus.LARGE_BUS_VEHICLE, strToCmpr);
    }
  }

  /**
   * Test getTypeOfVehicle when no large buses ar available.
   */
  @Test
  public void testGetTypeOfVehicleNoLargeBuses() {
    StorageFacility storageFacility = new StorageFacility(3, 0, 0, 0);
    BusStrategyNight busStrategyNight = new BusStrategyNight();
    String strToCmpr;
    for (int i = 0; i < 3; i++) {
      strToCmpr = busStrategyNight.getTypeOfVehicle(storageFacility);
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, strToCmpr); 
    }
    strToCmpr = busStrategyNight.getTypeOfVehicle(storageFacility); 
    assertNull(strToCmpr); 
  }

  /**
   * Test getTypeOfVehicle when facility is completely empty (no buses available).
   */
  @Test
  public void testGetTypeOfVehicleEmptyFacility() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 0, 0);
    BusStrategyNight busStrategyNight = new BusStrategyNight();
    String strToCmpr;
    for (int i = 0; i < 5; i++) {
      strToCmpr = busStrategyNight.getTypeOfVehicle(storageFacility);
      assertNull(strToCmpr);
    }
  }
}
