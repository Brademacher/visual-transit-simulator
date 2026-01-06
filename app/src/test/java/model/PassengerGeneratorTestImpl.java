package model;

import model.PassengerGenerator;
import model.Stop;

import java.util.List;

public class PassengerGeneratorTestImpl extends PassengerGenerator {

  public PassengerGeneratorTestImpl(List<Stop> stops, List<Double> probabilities) {
    super(stops, probabilities);
  }

  @Override
  public int generatePassengers() {
    return 0;
  }
}
