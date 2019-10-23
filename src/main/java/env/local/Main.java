package env.local;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle("com.syntel.vertx.heatsensor.HeatSensor", new DeploymentOptions().setInstances(4));
   // vertx.deployVerticle("com.syntel.vertx.Listener");
   // vertx.deployVerticle("com.syntel.vertx.SensorData");
    vertx.deployVerticle("com.syntel.vertx.RealTimeCollectorVertx");
  }
  
}