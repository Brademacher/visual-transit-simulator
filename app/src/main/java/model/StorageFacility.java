package model;

/**
 * Represents a vehicle storage facility in the simulation.
 * Tracks inactive buses and trains, and provides methods to
 * update or access their counts.
 */
public class StorageFacility {
  private int smallBusesNum;
  private int largeBusesNum;
  private int electricTrainsNum;
  private int dieselTrainsNum;

  /** Creates an empty storage facility. */
  public StorageFacility() {
    smallBusesNum = 0;
    largeBusesNum = 0;
    electricTrainsNum = 0;
    dieselTrainsNum = 0;
  }

  /**
   * Creates a storage facility with the given numbers.
   * @param smallBusesNum number of small buses
   * @param largeBusesNum number of large buses
   * @param electricTrainsNum number of electric trains
   * @param dieselTrainsNum number of diesel trains
   */
  public StorageFacility(int smallBusesNum, int largeBusesNum,
                         int electricTrainsNum, int dieselTrainsNum) {
    this.smallBusesNum = smallBusesNum;
    this.largeBusesNum = largeBusesNum;
    this.electricTrainsNum = electricTrainsNum;
    this.dieselTrainsNum = dieselTrainsNum;
  }

  /** Decrements the number of small buses in the storage facility. */
  public void decrementSmallBusesNum() {
    smallBusesNum--;
  }

  /** Decrements the number of large buses in the storage facility. */
  public void decrementLargeBusesNum() {
    largeBusesNum--;
  }

  /** Decrements the number of electric trains in the storage facility. */
  public void decrementElectricTrainsNum() {
    electricTrainsNum--;
  }

  /** Decrements the number of diesel trains in the storage facility. */
  public void decrementDieselTrainsNum() {
    dieselTrainsNum--;
  }

  /** Increments the number of small buses in the storage facility. */
  public void incrementSmallBusesNum() {
    smallBusesNum++;
  }

  /** Increments the number of large buses in the storage facility. */
  public void incrementLargeBusesNum() {
    largeBusesNum++;
  }

  /** Increments the number of electric trains in the storage facility. */
  public void incrementElectricTrainsNum() {
    electricTrainsNum++;
  }

  /** Increments the number of diesel trains in the storage facility. */
  public void incrementDieselTrainsNum() {
    dieselTrainsNum++;
  }

  /**
   * Gets the number of smalls buses in the storage facility.
   *
   * @return int for the number of small buses.
   */
  public int getSmallBusesNum() {
    return smallBusesNum;
  }

  /**
   * Sets the number of small buses in the storage facility.
   *
   * @param smallBusesNum number of small buses.
   */
  public void setSmallBusesNum(int smallBusesNum) {
    this.smallBusesNum = smallBusesNum;
  }

  /**
   * Gets the number of electric trains in the storage facility.
   *
   * @return int for the number of electric trains in the storage facility.
   */
  public int getElectricTrainsNum() {
    return electricTrainsNum;
  }

  /**
   * Sets the number of electric trains in the storage facility.
   *
   * @param electricTrainsNum number of electric trains.
   */
  public void setElectricTrainsNum(int electricTrainsNum) {
    this.electricTrainsNum = electricTrainsNum;
  }

  /**
   * Gets the number of large buses in the storage facility.
   *
   * @return int for the number of large buses.
   */
  public int getLargeBusesNum() {
    return largeBusesNum;
  }

  /**
   * Sets the number of large buses in the storage facility.
   *
   * @param largeBusesNum number of large buses.
   */
  public void setLargeBusesNum(int largeBusesNum) {
    this.largeBusesNum = largeBusesNum;
  }

  /**
   * Gets the number of diesel trains in the storage facility.
   *
   * @return int for the number of diesel trains.
   */
  public int getDieselTrainsNum() {
    return dieselTrainsNum;
  }

  /**
   * Sets the number of diesel trains in the storage facility.
   *
   * @param dieselTrainsNum number of diesel trains.
   */
  public void setDieselTrainsNum(int dieselTrainsNum) {
    this.dieselTrainsNum = dieselTrainsNum;
  }

}
