package webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.PassengerFactory;
// import edu.umn.cs.csci3081w.project.model.Vehicle;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import webserver.StartCommand;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;

public class StartCommandTest {

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
  }

  /**
   * Test command for starting the simulation.
   */
  @Test
  public void testExecute() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);

    StartCommand startCommand = new StartCommand(visualTransitSimulatorMock);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("numTimeSteps", 10);
    JsonArray timeBetweenVehicles = new JsonArray();
    timeBetweenVehicles.add(5);
    timeBetweenVehicles.add(7);
    commandFromClient.add("timeBetweenVehicles", timeBetweenVehicles);

    startCommand.execute(webServerSessionSpy, commandFromClient);

    verify(visualTransitSimulatorMock).setVehicleFactories(startCommand.getCurrentSimulationTime());
    verify(visualTransitSimulatorMock).start(Arrays.asList(5, 7), 10);
  }

  /**
   * Test that getCurrentSimulationTime() returns the current hour.
   */
  @Test
  public void testGetCurrentSimulationTime() {
    StartCommand startCommand = new StartCommand(mock(VisualTransitSimulator.class));
    int expectedHour = LocalDateTime.now().getHour();
    int actualHour = startCommand.getCurrentSimulationTime();
    assertEquals(expectedHour, actualHour);
  }
}
