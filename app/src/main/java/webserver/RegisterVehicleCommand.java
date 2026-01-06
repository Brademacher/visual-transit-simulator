package webserver;

import com.google.gson.JsonObject;
import model.VehicleInterface;
import java.util.List;

/**
 * A command class that extends SimulatorCommand.
 */
public class RegisterVehicleCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  /**
   * The RegisterVehicleCommand constructor.
   *
   * @param simulator the simulator being run
   */
  public RegisterVehicleCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Registers vehicle in observer.
   *
   * @param session current simulation session
   * @param command the get vehicles command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    List<VehicleInterface> activeVehicles = simulator.getActiveVehicles();
    int vehicleId = command.get("id").getAsInt();
    VehicleInterface vehicle = null;
    for (VehicleInterface activeVehicle : activeVehicles) {
      if (activeVehicle.getId() == vehicleId) {
        vehicle = activeVehicle;
      }
    }
    simulator.addObserver(vehicle);
  }
}
