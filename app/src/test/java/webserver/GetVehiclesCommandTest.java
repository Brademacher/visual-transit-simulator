package webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.DieselTrain;
import model.ElectricTrain;
import model.Issue;
import model.LargeBus;
import model.Line;
import model.PassengerFactory;
import model.Position;
import model.RandomPassengerGenerator;
import model.Route;
import model.SmallBus;
import model.Stop;
import model.VehicleInterface;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import webserver.GetVehiclesCommand;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;

public class GetVehiclesCommandTest {

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
   * Tests that GetVehiclesCommand correctly formats all vehicle types
   * and sends JSON to WebServerSession.
   */
  @Test
  public void testGetVehiclesExecute() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));

    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);

    List<Stop> stops = new ArrayList<>();
    stops.add(new Stop(0, "s1", new Position(0, 0)));
    stops.add(new Stop(1, "s2", new Position(1, 1)));

    List<Double> distances = new ArrayList<>();
    distances.add(1.0);

    List<Double> probabilities = new ArrayList<>();
    probabilities.add(0.1);

    Route route1 = new Route(1, "R1", stops, distances,
        new RandomPassengerGenerator(stops, probabilities));
    Route route2 = new Route(2, "R2", stops, distances,
        new RandomPassengerGenerator(stops, probabilities));

    List<VehicleInterface> vehicles = new ArrayList<>();
    vehicles.add(new SmallBus(1,
        new Line(10, "L1", "BUS", route1, route2, new Issue()), 10, 1.0));
    vehicles.add(new LargeBus(2,
        new Line(11, "L2", "BUS", route1, route2, new Issue()), 20, 1.0));
    vehicles.add(new ElectricTrain(3,
        new Line(12, "T1", "TRAIN", route1, route2, new Issue()), 30, 1.0));
    vehicles.add(new DieselTrain(4,
        new Line(13, "T2", "TRAIN", route1, route2, new Issue()), 40, 1.0));

    when(visualTransitSimulatorMock.getActiveVehicles()).thenReturn(vehicles);

    GetVehiclesCommand getVehiclesCommand = new GetVehiclesCommand(visualTransitSimulatorMock);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "getVehicles");

    getVehiclesCommand.execute(webServerSessionSpy, commandFromClient);

    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(webServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();

    assertEquals("updateVehicles", commandToClient.get("command").getAsString());

    JsonArray arr = commandToClient.getAsJsonArray("vehicles");
    assertEquals(4, arr.size());

    assertEquals("SMALL_BUS_VEHICLE", arr.get(0).getAsJsonObject().get("type").getAsString());
    assertEquals("LARGE_BUS_VEHICLE", arr.get(1).getAsJsonObject().get("type").getAsString());
    assertEquals("ELECTRIC_TRAIN_VEHICLE", arr.get(2).getAsJsonObject().get("type").getAsString());
    assertEquals("DIESEL_TRAIN_VEHICLE", arr.get(3).getAsJsonObject().get("type").getAsString());
  }
}
