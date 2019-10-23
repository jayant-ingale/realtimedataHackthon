package com.syntel.service;

import java.util.Optional;

import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class StaticHandlerService {

	  public void handleStatic(RoutingContext context) {
		  System.out.println("URI---"+context.request().uri());
	        String auctionId = context.request().getParam("id");
	        //context.response().putHeader("Location", "Hello Ind");
	       // Optional<Auction> auction = this.repository.getById(auctionId);
	        if (!StringUtil.isNullOrEmpty(auctionId)) {
	            context.response()
	                .putHeader("content-type", "application/json")
	                .setStatusCode(200)
	                .end(Json.encodePrettily(auctionId));
	        } else {
	            context.response()
	                .putHeader("content-type", "application/json")
	                .setStatusCode(404)
	                .end(Json.encodePrettily("Default end..."));
	        
	    }
	  }
	  
	  public void handleDefaultStatic(RoutingContext context) {
		  System.out.println("URI---"+context.request().uri());
	            context.response()
	                .putHeader("content-type", "application/json")
	                .setStatusCode(404)
	                .end(Json.encodePrettily("Default end..."));
	        
	  }
}
