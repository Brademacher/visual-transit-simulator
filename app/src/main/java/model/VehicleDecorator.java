package model;

import java.io.PrintStream;
import java.util.List;

/**
 * VehicleDecorator wraps an instance of a vehicle to move it along for the decorators.
 * This class Overrides methods from VehicleInterface
 */
public abstract class VehicleDecorator implements VehicleInterface {
  /*
      Override methods from the incoming vehicle.
      Essentially, we need to guarantee that the wrapped vehicle that we create will have the same
      properties of the Vehicle class
  */

  /**
   * The instance of our wrappee.
   */
  protected VehicleInterface wrappedVehicle;
  /**
   *  VehicleConcreteSubject for our wrappee.
   */
  protected VehicleConcreteSubject vehicleSubject;

  /**
   * Wraps the incoming vehicle into a VehicleDecorator.
   *
   * @param vehicle the vehicle that we are going to wrap
   */
  public VehicleDecorator(VehicleInterface vehicle) {
    this.wrappedVehicle = vehicle;
  }

  VehicleDecorator(Vehicle vehicle) {
    this.wrappedVehicle = vehicle;
  }
  
  @Override
  public void setVehicleSubject(VehicleConcreteSubject subject) {
    this.vehicleSubject = subject;
    wrappedVehicle.setVehicleSubject(subject);
  }

  protected VehicleConcreteSubject getVehicleSubject() {
    return vehicleSubject;
  }

  @Override
  public void report(PrintStream out) {
    wrappedVehicle.report(out);
  }

  @Override
  public boolean isTripComplete() {
    return wrappedVehicle.isTripComplete();
  }

  @Override
  public int loadPassenger(Passenger newPassenger) {
    return wrappedVehicle.loadPassenger(newPassenger);
  }

  @Override
  public void move() {
    wrappedVehicle.move();
  }

  @Override
  public void update() {
    wrappedVehicle.update();
  }

  @Override
  public boolean provideInfo() {
    return wrappedVehicle.provideInfo();
  }

  @Override
  public int getCurrentCO2Emission() {
    return wrappedVehicle.getCurrentCO2Emission();
  }

  @Override
  public int getId() {
    return wrappedVehicle.getId();
  }

  @Override
  public int getCapacity() {
    return wrappedVehicle.getCapacity();
  }

  @Override
  public double getSpeed() {
    return wrappedVehicle.getSpeed();
  }

  @Override
  public PassengerLoader getPassengerLoader() {
    return wrappedVehicle.getPassengerLoader();
  }

  @Override
  public PassengerUnloader getPassengerUnloader() {
    return wrappedVehicle.getPassengerUnloader();
  }

  @Override
  public List<Passenger> getPassengers() {
    return wrappedVehicle.getPassengers();
  }

  @Override
  public String getName() {
    return wrappedVehicle.getName();
  }

  @Override
  public Position getPosition() {
    return wrappedVehicle.getPosition();
  }

  @Override
  public Stop getNextStop() {
    return wrappedVehicle.getNextStop();
  }

  @Override
  public Line getLine() {
    return wrappedVehicle.getLine();
  }

  @Override
  public double getDistanceRemaining() {
    return wrappedVehicle.getDistanceRemaining();
  }

  @Override
  public void setName(String name) {
    wrappedVehicle.setName(name);
  }

  @Override
  public void setPosition(Position position) {
    wrappedVehicle.setPosition(position);
  }

  @Override
  public String getVehicleType() {
    return wrappedVehicle.getVehicleType();
  }
}
