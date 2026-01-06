package model;

import com.google.gson.JsonObject;
import webserver.WebServerSession;

public class SpyWebServerSession extends WebServerSession {

  public static JsonObject sentJson = null;
  public int sendCount = 0;

  @Override
  public void sendJson(JsonObject message) {
    this.sentJson = message;
    sendCount++;
  }
}
