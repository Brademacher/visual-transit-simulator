package webserver;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webserver.UpdateCommand;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;

public class UpdateCommandTest {

  private VisualTransitSimulator visSimMock;
  private UpdateCommand updateCommand;

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    visSimMock = mock(VisualTransitSimulator.class);
    updateCommand = new UpdateCommand(visSimMock);
  }

  /**
   * Tests that execute calls update on the VisualTransitSimulator.
   */
  @Test
  public void testExecuteCallsUpdate() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(org.mockito.Mockito.isA(JsonObject.class));

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "update");

    updateCommand.execute(webServerSessionSpy, commandFromClient);

    verify(visSimMock).update();
  }
}
