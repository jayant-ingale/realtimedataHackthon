package com.syntel.vertx.heatsensor;

import java.util.Random;
import java.util.UUID;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class HeatSensor extends AbstractVerticle {

	  private final Random random = new Random();
	  private final String id = UUID.randomUUID().toString();
	  private double temp = 21.0;

	  @Override
	  public void start() {
	    scheduleNextUpdate();
	  }

	  private void scheduleNextUpdate() {
	    vertx.setTimer(random.nextInt(5000) + 1000, this::update);
	  }

	  private void update(long tid) {
	    temp = temp + (delta() / 10);
	    JsonObject payload = new JsonObject()
	      .put("id", id)
	      .put("temp", temp);
	  //  System.out.println(" HeatSensor payload--"+payload);
	    vertx.eventBus().publish("sensor.updates", payload);// PUBLISH "sensor.updates"
	    //PUBLISH means The message will be delivered to all handlers registered to the address.
	    //https://vertx.io/docs/apidocs/io/vertx/core/eventbus/EventBus.html
	    scheduleNextUpdate();
	  }

	  private double delta() {
	    if (random.nextInt() > 0) {
	      return random.nextGaussian();
	    } else {
	      return -random.nextGaussian();
	    }
	  }
	  
	  
	  
}
