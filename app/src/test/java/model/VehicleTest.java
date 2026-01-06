package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VehicleTest {

  private SpyWebServerSession spyWebServerSession =  new SpyWebServerSession();
  private VehicleConcreteSubject vehicleConcreteSubject =
      new VehicleConcreteSubject(spyWebServerSession);
  private Vehicle testVehicle;
  private Route testRouteIn;
  private Route testRouteOut;
  Passenger testPassenger1;
  Passenger testPassenger2;
  Passenger testPassenger3;
  Passenger testPassenger4;


  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;

    RandomPassengerGenerator.DETERMINISTIC = true;

    testPassenger1 = new Passenger(3, "testPassenger1");
    testPassenger2 = new Passenger(2, "testPassenger2");
    testPassenger3 = new Passenger(1, "testPassenger3");
    testPassenger4 = new Passenger(1, "testPassenger4");

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

    testRouteIn = new Route(0, "testRouteIn",
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

    testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    testVehicle = new VehicleTestImpl(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0, new PassengerLoader(), new PassengerUnloader());

    vehicleConcreteSubject.attachObserver(testVehicle);
  }

  /**
   * Tests constructor.
   */
  @Test
  public void testConstructor() {
    assertEquals(1, testVehicle.getId());
    assertEquals("testRouteOut1", testVehicle.getName());
    assertEquals(3, testVehicle.getCapacity());
    assertEquals(1, testVehicle.getSpeed());
    assertEquals(testRouteOut, testVehicle.getLine().getOutboundRoute());
    assertEquals(testRouteIn, testVehicle.getLine().getInboundRoute());
  }

  /**
   * Tests if IsTripComplete function works properly.
   */
  @Test
  public void testIsTripComplete() {
    assertEquals(false, testVehicle.isTripComplete());
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    assertEquals(true, testVehicle.isTripComplete());
  }


  /**
   * Tests if loadPassenger function works properly.
   */
  @Test
  public void testLoadPassenger() {
    assertEquals(1, testVehicle.loadPassenger(testPassenger1));
    assertEquals(1, testVehicle.loadPassenger(testPassenger2));
    assertEquals(1, testVehicle.loadPassenger(testPassenger3));
    assertEquals(0, testVehicle.loadPassenger(testPassenger4));
  }


  /**
   * Tests if move function works properly.
   */
  @Test
  public void testMoveNoPassengers() {

    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());
    testVehicle.move();

    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals(null, testVehicle.getNextStop());

  }

  /**
   * Tests if update function works properly when no line issue.
   */
  @Test
  public void testUpdateNoLineIssue() {
    assertEquals(0, testPassenger1.getTimeOnVehicle());
    assertEquals(0, testPassenger2.getTimeOnVehicle());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger2.getWaitAtStop());
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());

    testVehicle.loadPassenger(testPassenger1);
    assertTrue(testPassenger1.isOnVehicle());
    assertEquals(1, testPassenger1.getTimeOnVehicle());

    assertFalse(testPassenger2.isOnVehicle());

    testVehicle.update();
    assertEquals(2, testPassenger1.getTimeOnVehicle());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger2.getWaitAtStop());
    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());
  }

  /**
   * Tests if update function works properly when line issue occurs.
   */
  @Test
  public void testUpdateWithLineIssue() {
    assertEquals(0, testPassenger1.getTimeOnVehicle());
    assertEquals(0, testPassenger2.getTimeOnVehicle());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger2.getWaitAtStop());
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());

    testVehicle.loadPassenger(testPassenger1);
    assertTrue(testPassenger1.isOnVehicle());
    assertEquals(1, testPassenger1.getTimeOnVehicle());
    assertFalse(testPassenger2.isOnVehicle());

    testVehicle.getLine().createIssue();
    testVehicle.update();
    assertEquals(2, testPassenger1.getTimeOnVehicle());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger2.getWaitAtStop());
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());
  }

  /**
   * Test to see if observer got attached.
   */
  @Test
  public void testProvideInfo() {
    testVehicle.provideInfo();
    JsonObject testOutput = SpyWebServerSession.sentJson;
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: " + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Testing if carbon emission history gets updated.
   */
  @Test
  public void testEmissionHistory() {

    testVehicle.update();
    testVehicle.provideInfo();
    JsonObject testOutput = SpyWebServerSession.sentJson;
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);

    testVehicle.update();
    testVehicle.provideInfo();
    testOutput = SpyWebServerSession.sentJson;
    command = testOutput.get("command").getAsString();
    expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    observedText = testOutput.get("text").getAsString();
    expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.243774,44.972392)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0, 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);

  }

  /**
   * Testing provide info when trip is complete.
   */
  @Test
  public void testProvideInfoTripComplete() {
    assertEquals(false, testVehicle.isTripComplete());
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    assertEquals(true, testVehicle.provideInfo());
  }

  /**
   * Clean up our variables after each test.
   */
  @AfterEach
  public void cleanUpEach() {
    testVehicle = null;
  }

}