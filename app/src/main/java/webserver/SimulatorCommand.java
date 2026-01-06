package webserver;

import com.google.gson.JsonObject;

/**
 * Abstract class representing a command that can be executed
 * in the Visual Transit Simulator via a web server session.
 */
public abstract class SimulatorCommand {

  /**
   * Used by subclasses to relay commands to the current simulation session.
   *
   * @param session the current simulation session.
   * @param command the command needing to be executed.
   */
  public abstract void execute(WebServerSession session, JsonObject command);
}
