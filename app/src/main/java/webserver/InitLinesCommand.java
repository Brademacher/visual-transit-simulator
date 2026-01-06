package webserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Line;

/**
 * Handles web requests to initialize line data in the simulation.
 * Collects all lines (each with inbound and outbound routes) and sends their
 * IDs, names, and types to the client as JSON for visualization.
 */
public class InitLinesCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  /**
   * Sets the simulator field to be used by the command.
   *
   * @param simulator the simulator currently being built.
   */
  public InitLinesCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Initializes the lines information for the simulation.
   *
   * @param session current simulation session
   * @param command the initialize routes command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    int numLines = simulator.getLines().size();
    JsonObject data = new JsonObject();
    data.addProperty("command", "initLines");
    data.addProperty("numLines", numLines);
    JsonArray linesArray = new JsonArray();
    for (int i = 0; i < simulator.getLines().size(); i++) {
      JsonObject s = new JsonObject();
      Line line = simulator.getLines().get(i);
      s.addProperty("id", line.getId());
      s.addProperty("name", line.getName());
      s.addProperty("type", line.getType());
      linesArray.add(s);
    }
    data.add("lines", linesArray);
    session.sendJson(data);
  }

}
