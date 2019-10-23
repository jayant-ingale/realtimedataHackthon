package com.syntel.service;

import java.util.Random;
import java.util.UUID;

import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class WeatherService {
	 private final Random random = new Random();
	  private final String id = UUID.randomUUID().toString();
	  private double temp = 21.0;
	  
	 public void handleTemprature(RoutingContext context)  {
		 
		
	        String temp = context.request().getParam("temp");
	        System.out.println("URI---"+context.request().uri()+"temp-"+temp);
	       // String temp = context.request().getParam("temp");
	        //context.response().putHeader("Location", "Hello Ind");
	       // Optional<Auction> auction = this.repository.getById(auctionId);
	        if (!StringUtil.isNullOrEmpty(temp)) {
	            context.response()
	                .putHeader("content-type", "application/json")
	                .setStatusCode(200)
	               // .end(Json.encodePrettily(update()));
	                .end(Json.encodePrettily(update()));
	        } else {
	            context.response()
	                .putHeader("content-type", "application/json")
	                .setStatusCode(404)
	                .end(Json.encodePrettily("Vehicle gps not found..."));
	        
	    }
	  }
	 
	 
	  private String update() {
		    temp = temp + (delta() / 10);
		    /*JsonObject payload = new JsonObject()
		      .put("id", id)
		      .put("temp", temp);*/
		    System.out.println(" WeatherService temp--"+temp);
		 //   vertx.eventBus().publish("sensor.updates", payload);// PUBLISH "sensor.updates"
		    //PUBLISH means The message will be delivered to all handlers registered to the address.
		    //https://vertx.io/docs/apidocs/io/vertx/core/eventbus/EventBus.html
		//    scheduleNextUpdate();
		    return temp+"";
		  }

		  private double delta() {
		    if (random.nextInt() > 0) {
		      return random.nextGaussian();
		    } else {
		      return -random.nextGaussian();
		    }
		  }
		  
}

