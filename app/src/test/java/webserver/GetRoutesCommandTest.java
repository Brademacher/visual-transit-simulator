package webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Issue;
import model.Line;
import model.PassengerFactory;
import model.Position;
import model.RandomPassengerGenerator;
import model.Route;
import model.Stop;
// import edu.umn.cs.csci3081w.project.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import webserver.GetRoutesCommand;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;

public class GetRoutesCommandTest {

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
   * Test command for getting all routes.
   */
  @Test
  public void testGetRoutesExecute() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);

    List<Stop> stops = new ArrayList<>();
    stops.add(new Stop(0, "s1", new Position(0, 0)));
    stops.add(new Stop(1, "s2", new Position(1, 1)));

    List<Double> distances = new ArrayList<>();
    distances.add(1.0);

    List<Double> probabilities = new ArrayList<>();
    probabilities.add(0.2);

    Route routeIn = new Route(10, "R_IN", stops, distances,
        new RandomPassengerGenerator(stops, probabilities));
    Route routeOut = new Route(20, "R_OUT", stops, distances,
        new RandomPassengerGenerator(stops, probabilities));

    List<Line> lines = new ArrayList<>();
    lines.add(new Line(100, "L1", "BUS", routeOut, routeIn, new Issue()));

    when(visualTransitSimulatorMock.getLines()).thenReturn(lines);

    GetRoutesCommand getRoutesCommand = new GetRoutesCommand(visualTransitSimulatorMock);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "getRoutes");

    getRoutesCommand.execute(webServerSessionSpy, commandFromClient);

    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(webServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();

    assertEquals("updateRoutes", commandToClient.get("command").getAsString());

    JsonArray arr = commandToClient.getAsJsonArray("routes");
    assertEquals(2, arr.size());

    assertEquals(20, arr.get(0).getAsJsonObject().get("id").getAsInt());
    assertEquals(10, arr.get(1).getAsJsonObject().get("id").getAsInt());
  }
}
