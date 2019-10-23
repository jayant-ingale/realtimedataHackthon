package com.syntel.service;

import java.util.Random;
import java.util.UUID;

import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class VehicleService {
	
	// *******************Heat*********************
	 private final Random random = new Random();
	  private final String id = UUID.randomUUID().toString();
	  private double temp = 21.0;
	 //**********************************************
	  
	 public void handleVehicleGPS(RoutingContext context) {
		 System.out.println("URI---"+context.request().uri());
		 
	        String vehicleId = context.request().getParam("vehicleId");
	        String vehicleSpeed = context.request().getParam("vehicleSpeed");
	        //context.response().putHeader("Location", "Hello Ind");
	       // Optional<Auction> auction = this.repository.getById(auctionId);
	        if (!StringUtil.isNullOrEmpty(vehicleId)) {
	            context.response()
	                .putHeader("content-type", "application/json")
	                .setStatusCode(200)
	                .end(Json.encodePrettily("Vehicle Id["+vehicleId+"]  vehicleSpeed["+vehicleSpeed)+"]");
	        } else {
	            context.response()
	                .putHeader("content-type", "application/json")
	                .setStatusCode(404)
	                .end(Json.encodePrettily("Vehicle gps not found..."));
	        
	    }
	  }
	 
	 public JsonObject handleVehicleHeat() {
		return  headPayload(random.nextInt(5000) + 1000);
	  }
	 
	 private JsonObject headPayload(long tid) {
		 long time =System.currentTimeMillis();
		    temp = temp + (delta() / 10);
		    JsonObject payload = new JsonObject()
		      .put("eventTime", time)
		      .put("vehicleTemp", temp);
		    //  System.out.println(" HeatSensor payload--"+payload);
		   // vertx.eventBus().publish("sensor.updates", payload);// PUBLISH "sensor.updates"
		    //PUBLISH means The message will be delivered to all handlers registered to the address.
		    //https://vertx.io/docs/apidocs/io/vertx/core/eventbus/EventBus.html
		   // scheduleNextUpdate();
		    return payload;
		  }

		  private double delta() {
		    if (random.nextInt() > 0) {
		      return random.nextGaussian();
		    } else {
		      return -random.nextGaussian();
		    }
		  }
}
