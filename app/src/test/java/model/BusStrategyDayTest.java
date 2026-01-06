package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusStrategyDayTest {

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
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    assertEquals(0, busStrategyDay.getCounter());
  }

  /**
   * Testing to get correct vehicle according to the strategy.
   */
  @Test
  public void testGetTypeOfVehicle() {
    StorageFacility storageFacility = new StorageFacility(1, 2, 0, 0);
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    String strToCmpr;
    for (int i = 0; i < 1; i++) {
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(LargeBus.LARGE_BUS_VEHICLE, strToCmpr);
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(LargeBus.LARGE_BUS_VEHICLE, strToCmpr);
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, strToCmpr);
    }
  }

  /**
   * Test getTypeOfVehicle when no large buses are available initially.
   */
  @Test
  public void testGetTypeOfVehicleNoLargeBuses() {
    StorageFacility storageFacility = new StorageFacility(3, 0, 0, 0);
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    for (int i = 0; i < 3; i++) {
      String result = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertNull(result);
    }
    assertEquals(0, busStrategyDay.getCounter());
  }

  /**
   * Test getTypeOfVehicle when no small buses are available.
   */
  @Test
  public void testGetTypeOfVehicleNoSmallBuses() {
    StorageFacility storageFacility = new StorageFacility(0, 3, 0, 0);
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    String result = busStrategyDay.getTypeOfVehicle(storageFacility);
    assertEquals("LARGE_BUS_VEHICLE", result);
    result = busStrategyDay.getTypeOfVehicle(storageFacility);
    assertEquals("LARGE_BUS_VEHICLE", result);
    result = busStrategyDay.getTypeOfVehicle(storageFacility);
    assertNull(result);
    result = busStrategyDay.getTypeOfVehicle(storageFacility);
    assertNull(result);
  }

  /**
   * Test getTypeOfVehicle when facility is completely empty (no buses available).
   */
  @Test
  public void testGetTypeOfVehicleEmptyFacility() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 0, 0);
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    for (int i = 0; i < 5; i++) {
      String result = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertNull(result);
    }
    assertEquals(0, busStrategyDay.getCounter());
  }
}
