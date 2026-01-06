package webserver;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webserver.PauseCommand;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;

public class PauseCommandTest {

  private VisualTransitSimulator visSimMock;
  private PauseCommand pauseCommand;

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    visSimMock = mock(VisualTransitSimulator.class);
    pauseCommand = new PauseCommand(visSimMock);
  }

  /**
   * Tests that execute calls togglePause on  VisualTransitSimulator.
   */
  @Test
  public void testExecuteCallsTogglePause() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(org.mockito.Mockito.isA(JsonObject.class));

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "pause");

    pauseCommand.execute(webServerSessionSpy, commandFromClient);

    verify(visSimMock).togglePause();
  }
}
