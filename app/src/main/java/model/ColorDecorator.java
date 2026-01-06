package model;

import com.google.gson.JsonObject;

/**
 * ColorDecorator extends VehicleDecorator to assign the
 * correct color to a vehicle based on its type.
 */
public class ColorDecorator extends VehicleDecorator {

  private int red;
  private int green;
  private int blue;
  private int alpha;

  /**
   * Constructs a ColorDecorator that is used
   * to wrap an incoming vehicle of type {@link VehicleInterface}.
   *
   * @param vehicle The vehicle that we are wrapping to decorate
   */
  public ColorDecorator(VehicleInterface vehicle) {
    super(vehicle);

    this.alpha = 255;

    String vtype = vehicle.getVehicleType();

    if (SmallBus.SMALL_BUS_VEHICLE.equals(vtype)) {
      this.red = 122;
      this.green = 0;
      this.blue = 25;
    } else if (LargeBus.LARGE_BUS_VEHICLE.equals(vtype)) {
      this.red = 239;
      this.green = 130;
      this.blue = 238;
    } else if (ElectricTrain.ELECTRIC_TRAIN_VEHICLE.equals(vtype)) {
      this.red = 60;
      this.green = 179;
      this.blue = 113;
    } else if (DieselTrain.DIESEL_TRAIN_VEHICLE.equals(vtype)) {
      this.red = 255;
      this.green = 204;
      this.blue = 51;
    } else {
      this.red = 0;
      this.green = 0;
      this.blue = 0;
    }
  }

  /**
   * Set the alpha value for the ColorDecorator from the incoming VehicleInterface.
   *
   * @param alpha integer value that we set as the alpha assignment
   */
  public void setAlpha(int alpha) {
    this.alpha = alpha;
  }

  /**
   * method to create json object for color.
   *
   * @return JsonObject with color specifications
   */
  public JsonObject getJsonColor() {
    JsonObject color = new JsonObject();
    color.addProperty("r", red);
    color.addProperty("g", green);
    color.addProperty("b", blue);
    color.addProperty("alpha", alpha);
    return color;
  }

  @Override
  public boolean provideInfo() {
    boolean isTripDone = wrappedVehicle.provideInfo();

    JsonObject data = new JsonObject();
    data.addProperty("command", "vehicleColorUpdate");
    data.addProperty("id", wrappedVehicle.getId());
    data.add("color", getJsonColor());

    if (getVehicleSubject() != null) {
      getVehicleSubject().getSession().sendJson(data);
    }

    return isTripDone;
  }

}
