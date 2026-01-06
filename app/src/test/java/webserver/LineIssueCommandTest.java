package webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import model.Issue;
import model.Line;
import model.PassengerFactory;
import model.RandomPassengerGenerator;
// import edu.umn.cs.csci3081w.project.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webserver.LineIssueCommand;
import webserver.VisualTransitSimulator;
import webserver.WebServerSession;

public class LineIssueCommandTest {

  /**
   * Setup Deterministic values before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  @Test
  public void testExecuteLineExists() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(org.mockito.Mockito.isA(JsonObject.class));
    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);

    LineIssueCommand lineIssueCommand = new LineIssueCommand(visualTransitSimulatorMock);

    Line testLine = new Line(0, "TestLine", Line.BUS_LINE,
        null, null, new Issue());
    List<Line> lines = new ArrayList<>();
    lines.add(testLine);
    when(visualTransitSimulatorMock.getLines()).thenReturn(lines);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("id", 0);

    lineIssueCommand.execute(webServerSessionSpy, commandFromClient);

    assertEquals(true, testLine.isIssueExist());
  }

  @Test
  public void testExecuteLineDoesNotExist() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(org.mockito.Mockito.isA(JsonObject.class));
    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);

    LineIssueCommand lineIssueCommand = new LineIssueCommand(visualTransitSimulatorMock);

    Line testLine = new Line(1, "TestLine", Line.BUS_LINE,
        null, null, new Issue());
    List<Line> lines = new ArrayList<>();
    lines.add(testLine);
    when(visualTransitSimulatorMock.getLines()).thenReturn(lines);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("id", 0);

    lineIssueCommand.execute(webServerSessionSpy, commandFromClient);

    assertEquals(false, testLine.isIssueExist());
  }
}
