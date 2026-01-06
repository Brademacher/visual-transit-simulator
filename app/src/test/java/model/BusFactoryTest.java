package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusFactoryTest {
  private StorageFacility storageFacility;
  private BusFactory busFactory;
  private BusFactory nightBusFactory;

  /**
   *  Setup bus factory.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
    storageFacility = new StorageFacility(3, 3, 0, 0);
    busFactory = new BusFactory(storageFacility, new Counter(), 9);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testConstructor() {
    assertTrue(busFactory.getGenerationStrategy() instanceof BusStrategyDay);
  }

  /**
   * Testing night train strategy constructor after time < 18.
   */
  @Test
  public void testLateNightBusStrategy() {
    nightBusFactory = new BusFactory(storageFacility, new Counter(), 19);
    assertTrue(nightBusFactory.getGenerationStrategy() instanceof BusStrategyNight);
  }

  /**
   * Testing night train strategy constructor before time > 7.
   */
  @Test
  public void testEarlyNightBusStrategy() {
    nightBusFactory = new BusFactory(storageFacility, new Counter(), 1);
    assertTrue(nightBusFactory.getGenerationStrategy() instanceof BusStrategyNight);
  }

  /**
   * Testing if generated vehicle is working according to strategy.
   */
  @Test
  public void testGenerateVehicle() {
    List<Stop> stopsIn = new ArrayList<Stop>();
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    stopsIn.add(stop1);
    stopsIn.add(stop2);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.843774422231134);
    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);

    Route testRouteIn = new Route(0, "testRouteIn",
        stopsIn, distancesIn, generatorIn);

    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop2);
    stopsOut.add(stop1);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.843774422231134);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.025);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Line line = new Line(10000, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue());
    Vehicle vehicle = busFactory.generateVehicle(line);
    assertTrue(vehicle instanceof LargeBus);

    Line line2 = new Line(10001, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue());
    Vehicle vehicle2 = busFactory.generateVehicle(line2);
    assertTrue(vehicle2 instanceof LargeBus);

    Line line3 = new Line(10002, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue());
    Vehicle vehicle3 = busFactory.generateVehicle(line3);
    assertTrue(vehicle3 instanceof SmallBus);

    Line line4 = new Line(10003, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue());
    Vehicle vehicle4 = busFactory.generateVehicle(line4);
    assertTrue(vehicle4 instanceof LargeBus);
  }

  /**
   * Testing if vehicle got returned.
   */
  @Test
  public void testReturnVehicleLargeBus() {
    List<Stop> stopsIn = new ArrayList<Stop>();
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    stopsIn.add(stop1);
    stopsIn.add(stop2);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.843774422231134);
    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);

    Route testRouteIn = new Route(0, "testRouteIn",
        stopsIn, distancesIn, generatorIn);

    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop2);
    stopsOut.add(stop1);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.843774422231134);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.025);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Bus testBus = new LargeBus(1, new Line(10000, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);

    Bus testBus2 = new SmallBus(2, new Line(10001, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);

    Train electricTrain = new ElectricTrain(3, new Line(10002, "testLine", "TRAIN",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    assertEquals(3, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(3, busFactory.getStorageFacility().getLargeBusesNum());
    busFactory.returnVehicle(testBus);
    busFactory.returnVehicle(testBus2);
    busFactory.returnVehicle(electricTrain); // Needed to complete branch coverage in returnVehicle
    assertEquals(4, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(4, busFactory.getStorageFacility().getLargeBusesNum());

  }
}
