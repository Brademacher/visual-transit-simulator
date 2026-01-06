package webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import model.Line;
import model.PassengerFactory;
import model.RandomPassengerGenerator;
import model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;

public class VisualTransitSimulatorTest {

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test constructor with null storage facility.
   */
  @Test
  public void testConstructorWithNullStorageFacility() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    assertNotNull(simulator.getLines());
  }

  /**
   * Test setVehicleFactories.
   */
  @Test
  public void testSetVehicleFactories() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    assertNotNull(simulator.getLines());
  }

  /**
   * Test start method.
   */
  @Test
  public void testStart() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(1);
    vehicleStartTimings.add(2);
    simulator.start(vehicleStartTimings, 50);
    assertNotNull(simulator.getLines());
  }

  /**
   * Test togglePause method.
   */
  @Test
  public void testTogglePause() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.togglePause();
    assertNotNull(simulator.getLines());
  }

  /**
   * Test togglePause 2 times  to cover both branches.
   */
  @Test
  public void testTogglePauseTwice() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.togglePause();
    simulator.togglePause();
    assertNotNull(simulator.getLines());
  }

  /**
   * Test update method with line issue.
   */
  @Test
  public void testUpdateWithLineIssue() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 5);
    
    List<Line> lines = simulator.getLines();
    Line lineSpy = spy(lines.get(0));
    when(lineSpy.isIssueExist()).thenReturn(true);
    lines.set(0, lineSpy);
    
    simulator.update();
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test update method without line issue.
   */
  @Test
  public void testUpdateWithoutLineIssue() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 5);
    simulator.update();
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test update beyond numTimeSteps.
   */
  @Test
  public void testUpdateBeyondTimeSteps() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 1);
    simulator.update();
    simulator.update();
    assertNotNull(simulator.getLines());
  }

  /**
   * Test getActiveVehicles.
   */
  @Test
  public void testGetActiveVehicles() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test addObserver.
   */
  @Test
  public void testAddObserver() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    Vehicle vehicleDummy = mock(Vehicle.class);
    simulator.addObserver(vehicleDummy);
    assertNotNull(simulator.getLines());
  }

  /**
   * Test update with paused simulation.
   */
  @Test
  public void testUpdateWhilePaused() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 5);
    simulator.togglePause();
    simulator.update();
    assertEquals(0, simulator.getActiveVehicles().size());
  }

  /**
   * Test update with timeSinceLastVehicle more  than 0.
   */
  @Test
  public void testUpdateWithTimeSinceLastVehicleGreaterThanZero() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(2);
    vehicleStartTimings.add(2);
    simulator.start(vehicleStartTimings, 10);
    simulator.update();
    simulator.update();
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test updae with train line type.
   */
  @Test
  public void testUpdateWithTrainLine() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 5);
    
    List<Line> lines = simulator.getLines();
    Line lineSpy = spy(lines.get(1));
    when(lineSpy.getType()).thenReturn("TRAIN_LINE");
    when(lineSpy.isIssueExist()).thenReturn(false);
    lines.set(1, lineSpy);
    
    simulator.update();
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test update with train line and issue.
   */
  @Test
  public void testUpdateWithTrainLineAndIssue() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 5);
    
    List<Line> lines = simulator.getLines();
    Line lineSpy = spy(lines.get(1));
    when(lineSpy.getType()).thenReturn("TRAIN_LINE");
    when(lineSpy.isIssueExist()).thenReturn(true);
    lines.set(1, lineSpy);
    
    simulator.update();
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test update with vehicle trip completion.
   */
  @Test
  public void testUpdateWithVehicleTripCompletion() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 100);
    
    for (int i = 0; i < 100; i++) {
      simulator.update();
    }
    
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test constructor with config that has storage facility.
   */
  @Test
  public void testConstructorWithStorageFacility() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config_storage.txt", sessionDummy);
    assertNotNull(simulator.getLines());
  }

  /**
   * Test update with train trip completion.
   */
  @Test
  public void testUpdateWithTrainTripCompletion() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 100);
    
    List<Line> lines = simulator.getLines();
    Line lineSpy = spy(lines.get(1));
    when(lineSpy.getType()).thenReturn("TRAIN_LINE");
    when(lineSpy.isIssueExist()).thenReturn(false);
    lines.set(1, lineSpy);
    
    for (int i = 0; i < 100; i++) {
      simulator.update();
    }
    
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test update with generated vehicle nulll and no issue.
   */
  @Test
  public void testUpdateWithGeneratedVehicleNull() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0);
    vehicleStartTimings.add(0);
    simulator.start(vehicleStartTimings, 5);
    
    List<Line> lines = simulator.getLines();
    Line lineSpy = spy(lines.get(0));
    when(lineSpy.isIssueExist()).thenReturn(false);
    lines.set(0, lineSpy);
    
    simulator.update();
    assertNotNull(simulator.getActiveVehicles());
  }

  /**
   * Test update with timeSinceLastVehicle greater than 0 and line issue.
   */
  @Test
  public void testUpdateWithTimeSinceLastVehicleAndIssue() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    VisualTransitSimulator simulator = new VisualTransitSimulator(
        "src/main/resources/config.txt", sessionDummy);
    simulator.setVehicleFactories(10);
    List<Integer> vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(2);
    vehicleStartTimings.add(2);
    simulator.start(vehicleStartTimings, 10);
    
    List<Line> lines = simulator.getLines();
    Line lineSpy = spy(lines.get(0));
    when(lineSpy.isIssueExist()).thenReturn(true);
    lines.set(0, lineSpy);
    
    simulator.update();
    simulator.update();
    simulator.update();
    assertNotNull(simulator.getActiveVehicles());
  }
}