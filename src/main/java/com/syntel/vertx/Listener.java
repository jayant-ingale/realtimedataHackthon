package com.syntel.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Listener extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(Listener.class);
  private final DecimalFormat format = new DecimalFormat("#.##");
  private final HashMap<String, Double> lastValues = new HashMap<>();
  @Override
  public void start() {
    EventBus bus = vertx.eventBus();
    bus.<JsonObject>consumer("sensor.updates", msg -> {
      JsonObject body = msg.body();
      String id = body.getString("id");
      String temp = format.format(body.getDouble("temp"));
      //logger.info("{} reports a temperature ~{}C", id, temp);
    //  System.out.println("{} reports a sensor.updates  ~{}C"+ id +" "+ temp);
    });
    
    bus.consumer("sensor.average", this::average);
  }
  
  private void average(Message<JsonObject> message) {

	    double avg = lastValues.values().stream()
	      .collect(Collectors.averagingDouble(Double::doubleValue));
	    JsonObject json = new JsonObject().put("average", avg);
	  //  System.out.println(" ListenerListenerListenerListener--SensorData avg json--"+json);
	    message.reply(json);
	  }
}