package webserver;

import model.BusFactory;
import model.ColorDecorator;
import model.Counter;
import model.DieselTrain;
import model.ElectricTrain;
import model.LargeBus;
import model.Line;
import model.SmallBus;
import model.StorageFacility;
import model.TrainFactory;
import model.VehicleConcreteSubject;
import model.VehicleFactory;
import model.VehicleInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * VisualTransitSimulator manages the state and updates of a transit simulation
 * with vehicles (buses/trains), lines, and routes.
 */
public class VisualTransitSimulator {

  private static boolean LOGGING = false;
  private int numTimeSteps = 0;
  private int simulationTimeElapsed = 0;
  private Counter counter;
  private List<Line> lines;
  private List<VehicleInterface> activeVehicles;
  private List<VehicleInterface> completedTripVehicles;
  private List<Integer> vehicleStartTimings;
  private List<Integer> timeSinceLastVehicle;
  private StorageFacility storageFacility;
  private WebServerSession webServerSession;
  private boolean paused = false;
  private VehicleFactory busFactory;
  private VehicleFactory trainFactory;
  private VehicleConcreteSubject vehicleConcreteSubject;

  /**
   * Constructor for Simulation.
   *
   * @param configFile       file containing the simulation configuration
   * @param webServerSession session associated with the simulation
   */
  public VisualTransitSimulator(String configFile, WebServerSession webServerSession) {
    this.webServerSession = webServerSession;
    this.counter = new Counter();
    ConfigManager configManager = new ConfigManager();
    configManager.readConfig(counter, configFile);
    this.lines = configManager.getLines();
    this.activeVehicles = new ArrayList<VehicleInterface>();
    this.completedTripVehicles = new ArrayList<VehicleInterface>();
    this.vehicleStartTimings = new ArrayList<Integer>();
    this.timeSinceLastVehicle = new ArrayList<Integer>();
    this.storageFacility = configManager.getStorageFacility();
    if (this.storageFacility == null) {
      this.storageFacility = new StorageFacility(Integer.MAX_VALUE, Integer.MAX_VALUE,
          Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    vehicleConcreteSubject = new VehicleConcreteSubject(webServerSession);

    if (VisualTransitSimulator.LOGGING) {
      System.out.println("////Simulation Lines////");
      for (int i = 0; i < lines.size(); i++) {
        lines.get(i).report(System.out);
      }
    }
  }

  /**
   * Initializes vehicle factory classes for the simulation.
   *
   * @param time time when the simulation was started
   */
  public void setVehicleFactories(int time) {
    this.busFactory = new BusFactory(storageFacility, counter, time);
    this.trainFactory = new TrainFactory(storageFacility, counter, time);
  }

  /**
   * Starts the simulation.
   *
   * @param vehicleStartTimings start timings of bus
   * @param numTimeSteps        number of time steps
   */
  public void start(List<Integer> vehicleStartTimings, int numTimeSteps) {
    this.vehicleStartTimings = vehicleStartTimings;
    this.numTimeSteps = numTimeSteps;
    for (int i = 0; i < vehicleStartTimings.size(); i++) {
      this.timeSinceLastVehicle.add(i, 0);
    }
    simulationTimeElapsed = 0;
  }

  /**
   * Toggles the pause state of the simulation.
   */
  public void togglePause() {
    paused = !paused;
  }

  /**
   * Updates the simulation at each step.
   */
  public void update() {
    if (!paused) {
      simulationTimeElapsed++;
      if (simulationTimeElapsed > numTimeSteps) {
        return;
      }
      System.out.println("~~~~The simulation time is now at time step "
          + simulationTimeElapsed + "~~~~");
      // generate vehicles
      for (int i = 0; i < timeSinceLastVehicle.size(); i++) {
        Line line = lines.get(i);

        // bus
        if (timeSinceLastVehicle.get(i) <= 0) {
          VehicleInterface generatedVehicle = null;
          if (line.getType().equals(Line.BUS_LINE) && !line.isIssueExist()) {
            generatedVehicle = busFactory.generateVehicle(line.shallowCopy());
            if (generatedVehicle != null) {
              generatedVehicle = new ColorDecorator(generatedVehicle);
            }

            // train
          } else if (line.getType().equals(Line.TRAIN_LINE) && !line.isIssueExist()) {
            generatedVehicle = trainFactory.generateVehicle(line.shallowCopy());
            if (generatedVehicle != null) {
              generatedVehicle = new ColorDecorator(generatedVehicle);
            }
          }
          if (line.getType().equals(Line.TRAIN_LINE) || line.getType().equals(Line.BUS_LINE)) {
            if (generatedVehicle != null && !line.isIssueExist()) {
              activeVehicles.add(generatedVehicle);
            }
            timeSinceLastVehicle.set(i, vehicleStartTimings.get(i));
            timeSinceLastVehicle.set(i, timeSinceLastVehicle.get(i) - 1);
          }
        } else {
          if (!line.isIssueExist()) {
            timeSinceLastVehicle.set(i, timeSinceLastVehicle.get(i) - 1);
          }
        }
      }
      // update vehicles
      for (int i = activeVehicles.size() - 1; i >= 0; i--) {
        VehicleInterface currVehicle = activeVehicles.get(i);
        currVehicle.update();
        if (currVehicle.isTripComplete()) {
          VehicleInterface completedTripVehicle = activeVehicles.remove(i);
          completedTripVehicles.add(completedTripVehicle);
          System.out.println("This are the completed trips: " + completedTripVehicle);
          if (completedTripVehicle.getVehicleType().equals(SmallBus.SMALL_BUS_VEHICLE)
              || completedTripVehicle.getVehicleType().equals(LargeBus.LARGE_BUS_VEHICLE)) {
            System.out.printf("Trip Complete, bus");
            busFactory.returnVehicle(completedTripVehicle);
          } else if (completedTripVehicle.getVehicleType().equals(ElectricTrain
              .ELECTRIC_TRAIN_VEHICLE) || completedTripVehicle.getVehicleType().equals(DieselTrain
              .DIESEL_TRAIN_VEHICLE)) {
            System.out.printf("Trip Complete, train");
            trainFactory.returnVehicle(completedTripVehicle);
          }
        } else {
          if (VisualTransitSimulator.LOGGING) {
            currVehicle.report(System.out);
          }
        }
      }
      // update lines
      for (int i = 0; i < lines.size(); i++) {
        Line currLine = lines.get(i);
        currLine.update();
        if (VisualTransitSimulator.LOGGING) {
          currLine.report(System.out);
        }
      }
      vehicleConcreteSubject.notifyObservers();
    }
  }

  public List<Line> getLines() {
    return lines;
  }

  public List<VehicleInterface> getActiveVehicles() {
    return activeVehicles;
  }

  /**
   * Registers an observer into the vehicle subject.
   *
   * @param vehicle the vehicle that starts observing
   */
  public void addObserver(VehicleInterface vehicle) {
    vehicleConcreteSubject.attachObserver(vehicle);
  }
}
