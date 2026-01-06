package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElectricTrainTest {

  private Train testTrain;
  private Route testRouteIn;
  private Route testRouteOut;

  private SpyWebServerSession spyWebServerSession =  new SpyWebServerSession();
  private VehicleConcreteSubject vehicleConcreteSubject =
      new VehicleConcreteSubject(spyWebServerSession);

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
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

    testTrain = new ElectricTrain(1,
        new Line(10000, "testLine", "TRAIN", testRouteOut, testRouteIn, new Issue()),
        3, 1.0);

    vehicleConcreteSubject.attachObserver(testTrain);
  }

  /**
   * Tests constructor.
   */
  @Test
  public void testConstructor() {
    assertEquals(1, testTrain.getId());
    assertEquals("testRouteOut1", testTrain.getName());
    assertEquals(3, testTrain.getCapacity());
    assertEquals(1, testTrain.getSpeed());
    assertEquals(testRouteOut, testTrain.getLine().getOutboundRoute());
    assertEquals(testRouteIn, testTrain.getLine().getInboundRoute());
  }

  /**
   * Tests if updateDistance function works properly.
   */
  @Test
  public void testReport() {
    testTrain.move();
    Passenger testPassenger = new Passenger(1, "Test Passenger");
    testTrain.loadPassenger(testPassenger);

    try {
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());

      ByteArrayOutputStream passengerOutput = new ByteArrayOutputStream();
      PrintStream passengerStream = new PrintStream(passengerOutput, true, charset.name());
      testPassenger.report(passengerStream);
      passengerStream.flush();
      String passengerData = new String(passengerOutput.toByteArray(), charset);
      passengerStream.close();
      passengerOutput.close();

      testTrain.report(testStream);
      outputStream.flush();
      String trainData = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();

      String expected =
          "####Electric Train Info Start####" + System.lineSeparator()
          + "ID: 1" + System.lineSeparator()
          + "Name: testRouteOut1" + System.lineSeparator()
          + "Speed: 1.0" + System.lineSeparator()
          + "Capacity: 3" + System.lineSeparator()
          + "Position: 44.97358,-93.235071" + System.lineSeparator()
          + "Distance to next stop: 0.843774422231134" + System.lineSeparator()
          + "****Passengers Info Start****" + System.lineSeparator()
          + "Num of passengers: 1" + System.lineSeparator()
          + passengerData
          + "****Passengers Info End****" + System.lineSeparator()
          + "####Electric Train Info End####" + System.lineSeparator();

      assertEquals(expected, trainData);

    } catch (IOException ioe) {
      fail(ioe);
    }
  }

  /**
   * Test the co2 calculation for a train.
   */
  @Test
  public void testCurrentCO2Emission() {
    assertEquals(0, testTrain.getCurrentCO2Emission());
    Passenger testPassenger1 = new Passenger(3, "testPassenger1");
    testTrain.loadPassenger(testPassenger1);
    assertEquals(0, testTrain.getCurrentCO2Emission());
  }

  /**
   * Test to see if observer got attached.
   */
  @Test
  public void testProvideInfo() {
    testTrain.update();
    testTrain.provideInfo();
    JsonObject testOutput = SpyWebServerSession.sentJson;
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: ELECTRIC_TRAIN_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Clean up our variables after each test.
   */
  @AfterEach
  public void cleanUpEach() {
    testTrain = null;
  }

}
