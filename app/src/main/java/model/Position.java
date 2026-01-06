package model;

/**
 * Represents a geographic position with longitude and latitude coordinates.
 */
public class Position {

  private double longitude;
  private double latitude;

  /** Constructor for position.
   *
   * @param longitude double for x coordinate.
   * @param latitude  double for y coordinate.
   */
  public Position(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  /**
   * Gets the longitude of a position.
   *
   * @return double value for longitude.
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Gets the latitude of a position.
   *
   * @return double value for longitude.
   */
  public double getLatitude() {
    return latitude;
  }

}
