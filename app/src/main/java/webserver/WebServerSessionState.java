package webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of a web server session.
 * Keeps track of available simulator commands in a session.
 */
public class WebServerSessionState {

  private Map<String, SimulatorCommand> commands;

  /**
   * Constructs a new WebServerSessionState with an empty command map.
   */
  public WebServerSessionState() {
    this.commands = new HashMap<String, SimulatorCommand>();
  }

  public Map<String, SimulatorCommand> getCommands() {
    return commands;
  }
}
