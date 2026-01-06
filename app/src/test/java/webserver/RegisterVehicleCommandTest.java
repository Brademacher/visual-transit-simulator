package webserver;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import model.PassengerFactory;
import model.RandomPassengerGenerator;
import model.Vehicle;
import model.VehicleInterface;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webserver.RegisterVehicleCommand;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;
// import org.mockito.ArgumentCaptor;

public class RegisterVehicleCommandTest {

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
   * Test registering a vehicle that exists in the active vehicle list.
   */
  @Test
  public void testExecuteVehicleExists() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);

    RegisterVehicleCommand registerVehicleCommand =
        new RegisterVehicleCommand(visualTransitSimulatorMock);

    Vehicle vehicleMock = mock(Vehicle.class);
    when(vehicleMock.getId()).thenReturn(1);

    List<VehicleInterface> activeVehicles = new ArrayList<>();
    activeVehicles.add(vehicleMock);
    when(visualTransitSimulatorMock.getActiveVehicles()).thenReturn(activeVehicles);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("id", 1);

    registerVehicleCommand.execute(webServerSessionSpy, commandFromClient);

    verify(visualTransitSimulatorMock).addObserver(vehicleMock);
  }

  /**
   * Test registering a vehicle that does not exist in the active vehicle list.
   */
  @Test
  public void testExecuteVehicleDoesNotExist() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);

    RegisterVehicleCommand registerVehicleCommand =
        new RegisterVehicleCommand(visualTransitSimulatorMock);

    Vehicle vehicleMock = mock(Vehicle.class);
    when(vehicleMock.getId()).thenReturn(2);

    List<VehicleInterface> activeVehicles = new ArrayList<>();
    activeVehicles.add(vehicleMock);
    when(visualTransitSimulatorMock.getActiveVehicles()).thenReturn(activeVehicles);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("id", 1);

    registerVehicleCommand.execute(webServerSessionSpy, commandFromClient);

    verify(visualTransitSimulatorMock).addObserver(null);
  }
}
