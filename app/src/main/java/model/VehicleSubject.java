package model;

/**
 * Interface for objects that can be observed by VehicleObserver.
 */
public interface VehicleSubject {

  /**
   * Adds an observer to the observer list.
   *
   * @param observer an observer for vehicle events.
   */
  public void attachObserver(VehicleObserver observer);

  /**
   * Removes an observer to the observer list.
   *
   * @param observer an observer for vehicle events.
   */
  public void detachObserver(VehicleObserver observer);

  /**
   * Notifies the observers in the observer list.
   */
  public void notifyObservers();
}
