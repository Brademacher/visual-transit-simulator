package webserver;

import com.google.gson.JsonObject;

/**
 * Represents a command that updates state of simulation.
 * This command is executed by web server to advance the simulation
 * by one step.
 */
public class UpdateCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  /**
   * Constructs an UpdateCommand with given simulator.
   *
   * @param simulator the visual transit simulator to be updated
   */
  public UpdateCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Updates the state of the simulation.
   *
   * @param session current simulation session
   * @param command the update simulation command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    simulator.update();
  }

}
