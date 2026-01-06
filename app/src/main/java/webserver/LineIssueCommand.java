package webserver;

import com.google.gson.JsonObject;
import model.Line;
import java.util.List;

/**
 * Command that handles line issues.
 * Will pause vehicles on a specific line for 10 time units.
 */
public class LineIssueCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  /**
   * Creates a LineIssueCommand using given simulator.
   *
   * @param simulator simulator instance to control
   */
  public LineIssueCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Injects issues in line.
   *
   * @param session current simulation session
   * @param command the initialize routes command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    List<Line> lines = simulator.getLines();
    int lineId = command.get("id").getAsInt();
    for (Line line : lines) {
      if (line.getId() == lineId) {
        line.createIssue();
      }
    }
  }

}
