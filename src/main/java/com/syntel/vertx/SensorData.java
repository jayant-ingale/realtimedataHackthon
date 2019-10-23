package com.syntel.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.stream.Collectors;

public class SensorData extends AbstractVerticle {

  private final HashMap<String, Double> lastValues = new HashMap<>();

  @Override
  public void start() {
    EventBus bus = vertx.eventBus();
    bus.consumer("sensor.updates", this::update);
    bus.consumer("sensor.average", this::average);
  }

  private void update(Message<JsonObject> message) {
    JsonObject json = message.body();
    lastValues.put(json.getString("id"), json.getDouble("temp"));
//    System.out.println(" SensorData update json--"+json);
  }

  private void average(Message<JsonObject> message) {
    double avg = lastValues.values().stream()
      .collect(Collectors.averagingDouble(Double::doubleValue));
    JsonObject json = new JsonObject().put("average", avg);
   // System.out.println(" SensorData avg json--"+json);
    message.reply(json);
  }
}