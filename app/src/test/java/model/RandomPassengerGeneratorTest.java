package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import model.PassengerFactory;
import model.Position;
import model.RandomPassengerGenerator;
import model.Stop;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RandomPassengerGeneratorTest {

  private RandomPassengerGenerator testRandomPassengerGenerator;


  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;

    List<Stop> stops = new ArrayList<Stop>();
    Stop testStop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop testStop2 = new Stop(1, "test stop 2", new Position(-93.25631, 44.963211));
    stops.add(testStop1);
    stops.add(testStop2);

    List<Double> probabilities = new ArrayList<Double>();
    probabilities.add(.15);

    testRandomPassengerGenerator = new RandomPassengerGenerator(stops, probabilities);
  }

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {

    assertEquals(0, testRandomPassengerGenerator.getStops()
        .get(0).getId());
    assertEquals("test stop 1", testRandomPassengerGenerator.getStops()
        .get(0).getName());
    assertEquals(-93.243774, testRandomPassengerGenerator.getStops()
        .get(0).getPosition().getLongitude());
    assertEquals(44.972392, testRandomPassengerGenerator.getStops()
        .get(0).getPosition().getLatitude());
    assertEquals(.15, testRandomPassengerGenerator.getProbabilities()
        .get(0));

  }

  /**
   * Test generatePassengers function.
   */
  @Test
  public void testGeneratePassengers() {
    testRandomPassengerGenerator.DETERMINISTIC = true;
    assertEquals(1, testRandomPassengerGenerator.generatePassengers());
  }

  /**
   * Test generatePassengers function doesn't generate on DETERMINISTIC = false.
   */
  @Test
  public void testGeneratePassengersNonDeterministic() {
    RandomPassengerGenerator testRandomPassengerGeneratorSpy = spy(testRandomPassengerGenerator);
    testRandomPassengerGeneratorSpy.DETERMINISTIC = false;

    when(testRandomPassengerGeneratorSpy.giveProb()).thenReturn(1.0);
    assertEquals(0, testRandomPassengerGeneratorSpy.generatePassengers());
  }

  /**
   * Clean up our variables after each test.
   */
  @AfterEach
  public void cleanUpEach() {
    testRandomPassengerGenerator = null;
  }


}
