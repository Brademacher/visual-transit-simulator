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

public class ColorDecoratorTest {

  private SpyWebServerSession spyWebServerSession =  new SpyWebServerSession();
  private VehicleConcreteSubject vehicleConcreteSubject =
      new VehicleConcreteSubject(spyWebServerSession);
  private VehicleInterface testVehicle;
  private ColorDecorator testColorDecorator;
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

    testColorDecorator = new ColorDecorator(testVehicle);

    vehicleConcreteSubject.attachObserver(testColorDecorator);
  }

  @Test
  public void testConstructor() {
    assertEquals(1, testColorDecorator.getId());
    assertEquals("testRouteOut1", testColorDecorator.getName());
    assertEquals(3, testColorDecorator.getCapacity());
    assertEquals(1, testColorDecorator.getSpeed());
    assertEquals(testRouteOut, testColorDecorator.getLine().getOutboundRoute());
    assertEquals(testRouteIn, testColorDecorator.getLine().getInboundRoute());
  }

  /**
   * Tests if IsTripComplete function works properly.
   */
  @Test
  public void testIsTripComplete() {
    assertEquals(false, testColorDecorator.isTripComplete());
    testColorDecorator.move();
    testColorDecorator.move();
    testColorDecorator.move();
    testColorDecorator.move();
    assertEquals(true, testColorDecorator.isTripComplete());
  }


  /**
   * Tests if loadPassenger function works properly.
   */
  @Test
  public void testLoadPassenger() {
    assertEquals(1, testColorDecorator.loadPassenger(testPassenger1));
    assertEquals(1, testColorDecorator.loadPassenger(testPassenger2));
    assertEquals(1, testColorDecorator.loadPassenger(testPassenger3));
    assertEquals(0, testColorDecorator.loadPassenger(testPassenger4));
  }


  /**
   * Tests if move function works properly.
   */
  @Test
  public void testMoveNoPassengers() {

    assertEquals("test stop 2", testColorDecorator.getNextStop().getName());
    assertEquals(1, testColorDecorator.getNextStop().getId());
    testColorDecorator.move();

    assertEquals("test stop 1", testColorDecorator.getNextStop().getName());
    assertEquals(0, testColorDecorator.getNextStop().getId());

    testColorDecorator.move();
    assertEquals("test stop 1", testColorDecorator.getNextStop().getName());
    assertEquals(0, testColorDecorator.getNextStop().getId());

    testColorDecorator.move();
    assertEquals("test stop 2", testColorDecorator.getNextStop().getName());
    assertEquals(1, testColorDecorator.getNextStop().getId());

    testColorDecorator.move();
    assertEquals(null, testColorDecorator.getNextStop());

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
    assertEquals("test stop 2", testColorDecorator.getNextStop().getName());
    assertEquals(1, testColorDecorator.getNextStop().getId());

    testColorDecorator.loadPassenger(testPassenger1);
    assertTrue(testPassenger1.isOnVehicle());
    assertEquals(1, testPassenger1.getTimeOnVehicle());

    assertFalse(testPassenger2.isOnVehicle());

    testColorDecorator.update();
    assertEquals(2, testPassenger1.getTimeOnVehicle());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger2.getWaitAtStop());
    assertEquals("test stop 1", testColorDecorator.getNextStop().getName());
    assertEquals(0, testColorDecorator.getNextStop().getId());
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
    assertEquals("test stop 2", testColorDecorator.getNextStop().getName());
    assertEquals(1, testColorDecorator.getNextStop().getId());

    testColorDecorator.loadPassenger(testPassenger1);
    assertTrue(testPassenger1.isOnVehicle());
    assertEquals(1, testPassenger1.getTimeOnVehicle());
    assertFalse(testPassenger2.isOnVehicle());

    testColorDecorator.getLine().createIssue();
    testColorDecorator.update();
    assertEquals(2, testPassenger1.getTimeOnVehicle());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger1.getWaitAtStop());
    assertEquals(0, testPassenger2.getWaitAtStop());
    assertEquals("test stop 2", testColorDecorator.getNextStop().getName());
    assertEquals(1, testColorDecorator.getNextStop().getId());
  }

  /**
   * Test to see if observer got attached.
   * Should only be looking at the Json Data passed by the decorator.
   */
  @Test
  public void testProvideInfo() {
    testColorDecorator.provideInfo();
    JsonObject testOutput = SpyWebServerSession.sentJson;

    String command = testOutput.get("command").getAsString();
    String expectedCommand = "vehicleColorUpdate";
    assertEquals(expectedCommand, command);

    JsonObject color = testOutput.getAsJsonObject("color");
    assertEquals(0,   color.get("r").getAsInt());
    assertEquals(0, color.get("g").getAsInt());
    assertEquals(0,  color.get("b").getAsInt());
    assertEquals(255,           color.get("alpha").getAsInt());
  }

  /**
   * Testing setAlpha() properly updates the alpha value.
   */
  @Test
  public void testSetAlpha() {
    testColorDecorator.provideInfo();
    JsonObject testOutput = SpyWebServerSession.sentJson;

    String command = testOutput.get("command").getAsString();
    String expectedCommand = "vehicleColorUpdate";
    assertEquals(expectedCommand, command);

    JsonObject color = testOutput.getAsJsonObject("color");
    assertEquals(0,   color.get("r").getAsInt());
    assertEquals(0, color.get("g").getAsInt());
    assertEquals(0,  color.get("b").getAsInt());
    assertEquals(255,           color.get("alpha").getAsInt());

    testColorDecorator.setAlpha(0);
    testColorDecorator.provideInfo();
    testOutput = SpyWebServerSession.sentJson;

    command = testOutput.get("command").getAsString();
    expectedCommand = "vehicleColorUpdate";
    assertEquals(expectedCommand, command);

    color = testOutput.getAsJsonObject("color");
    assertEquals(0,   color.get("r").getAsInt());
    assertEquals(0, color.get("g").getAsInt());
    assertEquals(0,  color.get("b").getAsInt());
    assertEquals(0,           color.get("alpha").getAsInt());

  }

  /**
   * Testing provide info when trip is complete.
   */
  @Test
  public void testProvideInfoTripComplete() {
    assertEquals(false, testColorDecorator.isTripComplete());
    testColorDecorator.move();
    testColorDecorator.move();
    testColorDecorator.move();
    testColorDecorator.move();
    assertEquals(true, testColorDecorator.provideInfo());
  }

  /**
   * Testing the VehicleDecorator getPosition().
   * Should be the same position as the
   * testVehicle position, since ColorDecorator is just a wrapper.
   */
  @Test
  public void testGetPosition() {
    Position position = testColorDecorator.getPosition();
    assertEquals(testVehicle.getPosition(), position);
  }

  /**
   * Testing vehiclePassengers isn't altered by the decorator.
   */
  @Test
  public void testGetPassengers() {
    List<Passenger> cdPassengers = testColorDecorator.getPassengers();
    assertEquals(testVehicle.getPassengers().size(), cdPassengers.size());
  }

  /**
   * Testing Vehicle.getPassengerLoader() to VehicleDecorator.getPassengerLoader().
   */
  @Test
  public void testGetPassengerLoader() {
    PassengerLoader cdPassengerLoader = testColorDecorator.getPassengerLoader();
    assertEquals(testVehicle.getPassengerLoader(), cdPassengerLoader);
  }

  /**
   * Testing Vehicle.getPassengerUnloader() to VehicleDecorator.getPassengerUnloader().
   */
  @Test
  public void testGetPassengerUnloader() {
    PassengerUnloader cdPassengerUnloader = testColorDecorator.getPassengerUnloader();
    assertEquals(testVehicle.getPassengerUnloader(), cdPassengerUnloader);
  }

  @AfterEach
  public void tearDown() {}
}
