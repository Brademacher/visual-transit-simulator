package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for unloading passengers from a vehicle.
 * Removes passengers whose destination matches the current stop.
 */
public class PassengerUnloader {

  /**
   * Unloads passengers.
   *
   * @param currentStop Current stop
   * @param passengers  list of passengers
   * @return number of passengers unloaded
   */
  public int unloadPassengers(List<Passenger> passengers, Stop currentStop) {
    int passengersUnloaded = 0;
    List<Passenger> copyList = new ArrayList<>();
    for (Passenger p : passengers) {
      if (p.getDestination() == currentStop.getId()) {
        passengersUnloaded++;
      } else {
        copyList.add(p);
      }
    }
    passengers.clear();
    passengers.addAll(copyList);
    return passengersUnloaded;
  }
}
