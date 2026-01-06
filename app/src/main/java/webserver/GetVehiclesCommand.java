package webserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.ColorDecorator;
import model.VehicleInterface;

import java.util.List;

/**
 * Handles web requests to retrieve all active vehicles (buses and trains).
 * Sends each vehicle's ID, type, passenger count, CO2 emissions, and position as JSON.
 */
public class GetVehiclesCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  /**
   * Constructs a new GetVehiclesCommand.
   *
   * @param simulator the VisualTransitSimulator instance
   *                  used to access vehicle data from the simulation
   */
  public GetVehiclesCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Retrieves vehicles information from the simulation.
   *
   * @param session current simulation session
   * @param command the get vehicles command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    List<VehicleInterface> vehicles = simulator.getActiveVehicles();
    JsonObject data = new JsonObject();
    data.addProperty("command", "updateVehicles");

    JsonArray vehiclesArray = new JsonArray();

    for (int i = 0; i < vehicles.size(); i++) {
      VehicleInterface currVehicle = vehicles.get(i);
      JsonObject s = new JsonObject();

      s.addProperty("id", currVehicle.getId());
      s.addProperty("numPassengers", currVehicle.getPassengers().size());
      s.addProperty("capacity", currVehicle.getCapacity());
      s.addProperty("type", currVehicle.getVehicleType());
      s.addProperty("co2", currVehicle.getCurrentCO2Emission());

      JsonObject positionJsonObject = new JsonObject();
      positionJsonObject.addProperty("longitude", currVehicle.getPosition().getLongitude());
      positionJsonObject.addProperty("latitude", currVehicle.getPosition().getLatitude());
      s.add("position", positionJsonObject);

      if (currVehicle instanceof ColorDecorator) {
        ColorDecorator color = (ColorDecorator) currVehicle;
        if (currVehicle.getLine().isIssueExist()) {
          color.setAlpha(155);
        } else {
          color.setAlpha(255);
        }
        s.add("color", color.getJsonColor());
      }
      vehiclesArray.add(s);
    }

    data.add("vehicles", vehiclesArray);
    session.sendJson(data);
  }
}
