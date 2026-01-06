package webserver;

import com.google.gson.JsonObject;

/**
 * Command that pauses or resumes the simulation.
 */
public class PauseCommand extends SimulatorCommand {
  private VisualTransitSimulator visSim;

  /**
   * Creates a PauseCommand using the given simulator.
   *
   * @param visSim simulator instance to control
   */
  public PauseCommand(VisualTransitSimulator visSim) {
    this.visSim = visSim;
  }

  /**
   * Tells the simulator to pause the simulation.
   *
   * @param session object representing the simulation session
   * @param command object containing the original command
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    visSim.togglePause();
  }
}
