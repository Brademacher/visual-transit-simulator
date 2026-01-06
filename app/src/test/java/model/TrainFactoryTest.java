package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrainFactoryTest {
  private StorageFacility storageFacility;
  private TrainFactory dayTrainFactory;
  private TrainFactory nightTrainFactory;

  /**
   * Setup operations.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
    storageFacility = new StorageFacility(0, 0, 5, 5);
    dayTrainFactory = new TrainFactory(storageFacility, new Counter(), 9);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testConstructor() {
    assertTrue(dayTrainFactory.getGenerationStrategy() instanceof TrainStrategyDay);
  }

  /**
   * Testing night train strategy constructor after time < 18.
   */
  @Test
  public void testLateNightTrainStrategy() {
    nightTrainFactory = new TrainFactory(storageFacility, new Counter(), 19);
    assertTrue(nightTrainFactory.getGenerationStrategy() instanceof TrainStrategyNight);
  }

  /**
   * Testing night train strategy constructor before time > 7.
   */
  @Test
  public void testEarlyNightTrainStrategy() {
    nightTrainFactory = new TrainFactory(storageFacility, new Counter(), 1);
    assertTrue(nightTrainFactory.getGenerationStrategy() instanceof TrainStrategyNight);
  }

  /**
   * Testing if generateVehicle is working according to strategy (in this test, Day strategy).
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

    Line line = new Line(10000, "testLine", "TRAIN", testRouteOut, testRouteIn,
        new Issue());

    Vehicle vehicle1 = dayTrainFactory.generateVehicle(line);
    assertTrue(vehicle1 instanceof ElectricTrain);

    Line line2 = new Line(10001, "testLine", "TRAIN", testRouteOut, testRouteIn,
        new Issue());
    Vehicle vehicle2 = dayTrainFactory.generateVehicle(line2);
    assertTrue(vehicle2 instanceof ElectricTrain);

    Line line3 = new Line(10002, "testLine", "TRAIN", testRouteOut, testRouteIn,
        new Issue());
    Vehicle vehicle3 = dayTrainFactory.generateVehicle(line3);
    assertTrue(vehicle3 instanceof ElectricTrain);

    Line line4 = new Line(10003, "testLine", "TRAIN", testRouteOut, testRouteIn,
        new Issue());
    Vehicle vehicle4 = dayTrainFactory.generateVehicle(line4);
    assertTrue(vehicle4 instanceof DieselTrain);
  }

  /**
   * Testing if vehicles got returned.
   */
  @Test
  public void testReturnVehicleElectricTrain() {
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

    Train electricTrain = new ElectricTrain(1, new Line(10000, "testLine", "TRAIN",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    Train dieselTrain = new DieselTrain(2, new Line(10001, "testLine", "TRAIN",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    Bus testBus = new LargeBus(3, new Line(10002, "testLine", "TRAIN",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    assertEquals(5, dayTrainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(5, dayTrainFactory.getStorageFacility().getDieselTrainsNum());
    dayTrainFactory.returnVehicle(electricTrain);
    dayTrainFactory.returnVehicle(dieselTrain);
    dayTrainFactory.returnVehicle(testBus); // Needed to complete branch coverage in returnVehicle
    assertEquals(6, dayTrainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(6, dayTrainFactory.getStorageFacility().getDieselTrainsNum());

  }
}
